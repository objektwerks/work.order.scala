package workorder

import com.github.blemale.scaffeine.{Cache, Scaffeine}
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import java.nio.file.Files
import java.nio.file.Paths

import scalikejdbc.*
import scala.concurrent.duration.FiniteDuration
import scala.io.{Codec, Source}
import scala.util.Using

object Store:
  def cache(minSize: Int,
            maxSize: Int,
            expireAfter: FiniteDuration): Cache[String, String] =
    Scaffeine()
      .initialCapacity(minSize)
      .maximumSize(maxSize)
      .expireAfterWrite(expireAfter)
      .build[String, String]()

  def dirs(conf: Config): Unit =
    Files.createDirectories(Paths.get(conf.getString("dir")))
    Files.createDirectories(Paths.get(conf.getString("imagesDir")))
    Files.createDirectories(Paths.get(conf.getString("logsDir")))

final class Store(conf: Config, cache: Cache[String, String]) extends LazyLogging:
  private val url = conf.getString("db.url")
  private val user = conf.getString("db.user")
  private val password = conf.getString("db.password")
  private val initialSize = conf.getInt("db.initialSize")
  private val maxSize = conf.getInt("db.maxSize")
  private val connectionTimeoutMillis = conf.getLong("db.connectionTimeoutMillis")
  private val settings = ConnectionPoolSettings(
    initialSize = initialSize,
    maxSize = maxSize,
    connectionTimeoutMillis = connectionTimeoutMillis
  )

  ConnectionPool.singleton(url, user, password, settings)

  def ddl(file: String): Unit =
    val statements = Using( Source.fromFile(file, Codec.UTF8.name) ) { source =>
      source.mkString.split(";").map(_.trim)
    }.get
    statements.foreach { statement =>
      DB localTx { implicit session =>
        println(s"$statement")
        sql"$statement".execute()
      }
    }

  def isLicenseValid(license: String): Boolean =
    cache.getIfPresent(license) match
      case Some(_) =>
        logger.debug(s"*** store cache get: $license")
        true
      case None =>
        val optionalLicense = DB readOnly { implicit session =>
          sql"select license from user where license = $license"
            .map(rs => rs.string("license"))
            .single()
        }
        if optionalLicense.isDefined then
          cache.put(license, license)
          logger.debug(s"*** store cache put: $license")
          true
        else false

  def listWorkOrders(userId: Int): List[WorkOrder] =
    DB readOnly { implicit session =>
      sql"select * from work_order where homeownerId = $userId or serviceProviderId = $userId order by opened desc"
        .map(rs => WorkOrder(
          rs.int("number"),
          rs.int("homeownerId"),
          rs.int("serviceProviderId"),
          rs.string("title"),
          rs.string("issue"),
          rs.string("streetAddress"),
          rs.string("imageUrl"),
          rs.string("resolution"),
          rs.string("opened"),
          rs.string("closed")))
        .list()
    }

  def listUsersByRole(role: String): List[User] =
    DB readOnly { implicit session =>
      sql"select * from user where role = $role order by name asc"
        .map(rs => User(
          rs.int("id"),
          rs.string("role"),
          rs.string("name"),
          rs.string("emailAddress"),
          rs.string("streetAddress"),
          rs.string("registered"),
          rs.string("pin"),
          rs.string("license")))
        .list()
    }

  def listEmailAddressesByIds(homeownerId: Int, serviceProviderId: Int): List[String] =
    DB readOnly { implicit session =>
      sql"select emailAddress from user where id in ($homeownerId, $serviceProviderId)"
        .map(rs => rs.string("emailAddress"))
        .list()
    }

  def getUserByEmailAddressPin(emailAddress: String, pin: String): Option[User] =
    DB readOnly { implicit session =>
      sql"select * from user where emailAddress = $emailAddress and pin = $pin"
        .map(rs => User(
          rs.int("id"),
          rs.string("role"),
          rs.string("name"),
          rs.string("emailAddress"),
          rs.string("streetAddress"),
          rs.string("registered"),
          rs.string("pin"),
          rs.string("license")))
        .single()
    }

  def saveUser(user: User): Unit =
    DB localTx { implicit session =>
      sql"""
        update user set role = ${user.role}, name = ${user.name}, emailAddress = ${user.emailAddress}, streetAddress = ${user.streetAddress},
        registered = ${user.registered}, pin = ${user.pin} where id = ${user.id}
      """
      .update()
    }
    ()   

  def addWorkOrder(workOrder: WorkOrder): WorkOrder =
    val number = DB localTx { implicit session =>
      sql"""
        insert into work_order(homeownerId, serviceProviderId, title, issue, streetAddress, imageUrl, resolution, opened, closed) 
        values(${workOrder.homeownerId}, ${workOrder.serviceProviderId}, ${workOrder.title}, ${workOrder.issue}, ${workOrder.streetAddress},
        ${workOrder.imageUrl}, ${workOrder.resolution}, ${workOrder.opened}, ${workOrder.closed})
        """
      .update()
    }
    workOrder.copy(number = number)

  def saveWorkOrder(workOrder: WorkOrder): Unit =
    DB localTx { implicit session =>
      sql"""
        update work_order set homeownerId = ${workOrder.homeownerId}, serviceProviderId = ${workOrder.serviceProviderId},
        title = ${workOrder.title}, issue = ${workOrder.issue}, streetAddress = ${workOrder.streetAddress}, imageUrl = ${workOrder.imageUrl},
        resolution = ${workOrder.resolution}, opened = ${workOrder.opened}, closed = ${workOrder.closed} where number = ${workOrder.number}
      """
      .update()
    }
    ()

  def addUser(user: User): User =
    val id = DB localTx { implicit session =>
      sql"""
        insert into user (role, name, emailAddress, streetAddress, registered, pin) 
        values(${user.role}, ${user.name}, ${user.emailAddress}, ${user.streetAddress}, ${user.registered}, ${user.pin})
      """
      .update()
    }
    user.copy(id = id) 