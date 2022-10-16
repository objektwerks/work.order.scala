package poolmate

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.*
import scala.sys.process.Process

import Validator.*

class HandlerTest extends AnyFunSuite with Matchers with LazyLogging:
  Process("mysql -u root < ddl.sql").run().exitValue()

  val conf = ConfigFactory.load("test.server.conf")

  val emailer = Emailer(conf)
  val store = Store(conf, Store.cache(minSize = 4, maxSize = 10, expireAfter = 24.hour))
  val service = Service(emailer, store)
  val handler = Handler(service)

  test("handler") {
    // TODO
  }

  def testRegister(handler: Handler): Unit = ()

  def testLogin(handler: Handler): Unit = ()

  def testSaveUser(handler: Handler): Unit = ()

  def testAddWorkOrder(handler: Handler): Unit = ()

  def testSaveWorkOrder(handler: Handler): Unit = ()

  def testListWorkOrders(handler: Handler): Unit = ()