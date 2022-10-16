package poolmate

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import java.time.Instant

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
    val serviceProviderEmail = conf.getString("email.serviceProviderEmail")
    val homeownerEmail = conf.getString("email.homeownerEmail")

    // register
    val serviceProviderRegister = Register(Roles.serviceProvider, "lawncare service", serviceProviderEmail, "123 green rd")
    val serviceProviderPin = handler.handle(serviceProviderRegister) match
      case registered: Registered => registered.success shouldBe true; registered.pin
      case _ => fail()

    val homeownerRegister = Register(Roles.homeowner, "fred flintstone", homeownerEmail, "345 stone st")
    val homeownerPin = handler.handle(homeownerRegister) match
      case registered: Registered => registered.success shouldBe true; registered.pin
      case _ => fail()

    // login
    val serviceProviderLogin = Login(serviceProviderEmail, serviceProviderPin)
    val serviceProviderLoggedIn = handler.handle(serviceProviderLogin) match
      case loggedIn: LoggedIn => loggedIn.success shouldBe true; loggedIn
      case _ => fail()
    
    val homeownerLogin = Login(homeownerEmail, homeownerPin)
    val homeownerLoggedIn = handler.handle(homeownerLogin) match
      case loggedIn: LoggedIn => loggedIn.success shouldBe true; loggedIn
      case _ => fail()

    // work order add
    var workOrder = WorkOrder(0, homeownerLoggedIn.user.id, serviceProviderLoggedIn.user.id, "sprinkler", "345 stone st", "broken", "", "", Instant.now().toString(), "")
    workOrder = handler.handle(AddWorkOrder(workOrder, homeownerLoggedIn.user.license)) match
      case workOrderAdded: WorkOrderAdded => workOrderAdded.success shouldBe true; workOrder.copy(number = workOrderAdded.number)
      case _ => fail()

    // work order save
    workOrder = workOrder.copy(resolution = "fixed")
    workOrder.copy(closed = Instant.now().toString())
    handler.handle(SaveWorkOrder(workOrder, homeownerLoggedIn.user.license)) match
      case workOrderSaved: WorkOrderSaved => workOrderSaved.success shouldBe true; workOrderSaved
      case _ => fail()
    
    // user save
    handler.handle(SaveUser(serviceProviderLoggedIn.user)) match
      case userSaved: UserSaved => userSaved.success shouldBe true
      case _ => fail()

    handler.handle(SaveUser(homeownerLoggedIn.user)) match
      case userSaved: UserSaved => userSaved.success shouldBe true
      case _ => fail()

    // work orders list
    handler.handle(ListWorkOrders(homeownerLoggedIn.user.id, homeownerLoggedIn.user.license)) match
      case workOrdersListed: WorkOrdersListed => workOrdersListed.success shouldBe true; workOrdersListed.workOrders.size shouldBe 1
      case _ => fail()
  }