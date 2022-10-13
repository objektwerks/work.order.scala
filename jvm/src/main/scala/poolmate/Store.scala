package poolmate

import com.github.blemale.scaffeine.{Cache, Scaffeine}
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import scalikejdbc.*
import scala.concurrent.duration.FiniteDuration

object Store:
  def cache(minSize: Int,
            maxSize: Int,
            expireAfter: FiniteDuration): Cache[String, String] =
    Scaffeine()
      .initialCapacity(minSize)
      .maximumSize(maxSize)
      .expireAfterWrite(expireAfter)
      .build[String, String]()

final class Store(conf: Config,
                  cache: Cache[String, String]) extends LazyLogging:
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

  def isLicenseValid(license: String): Boolean =
    cache.getIfPresent(license) match
      case Some(_) =>
        logger.debug(s"*** store cache get: $license")
        true
      case None =>
        val optionalLicense = DB readOnly { implicit session =>
          sql"select license from account where license = $license"
            .map(rs => rs.string("license"))
            .single()
        }
        if optionalLicense.isDefined then
          cache.put(license, license)
          logger.debug(s"*** store cache put: $license")
          true
        else false
