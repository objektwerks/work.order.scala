package poolmate

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.*

import Validator.*

class HandlerTest extends AnyFunSuite with Matchers with LazyLogging:
  val conf = ConfigFactory.load("test.server.conf")

  val emailer = Emailer(conf)
  Store.dirs(conf)
  val store = Store(conf, Store.cache(minSize = 2, maxSize = 3, expireAfter = 1.hour))
  val service = Service(emailer, store)
  val handler = Handler(service)

  test("handler") {
    println("*** running integration test ...")

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
    val serviceProviderLoggedIn = handler.handle(Login(serviceProviderEmail, serviceProviderPin)) match
      case loggedIn: LoggedIn => loggedIn.success shouldBe true; loggedIn
      case _ => fail()
    
    val homeownerLoggedIn = handler.handle(Login(homeownerEmail, homeownerPin)) match
      case loggedIn: LoggedIn => loggedIn.success shouldBe true; loggedIn
      case _ => fail()

    // work order add
    var workOrder = WorkOrder(
      number = 0,
      homeownerId = homeownerLoggedIn.user.id,
      serviceProviderId = serviceProviderLoggedIn.user.id,
      title = "sprinkler",
      issue = "broken",
      streetAddress = "345 stone st",
      imageUrl = "",
      resolution = "",
      opened = DateTime.now,
      closed = "")
    val number = handler.handle(AddWorkOrder(workOrder, homeownerLoggedIn.user.license)) match
      case workOrderAdded: WorkOrderAdded =>
        workOrderAdded.success shouldBe true
        workOrderAdded.number
      case _ => fail()

    // work order save
    workOrder = workOrder.copy(number = number, resolution = "fixed", closed = DateTime.now)
    handler.handle(SaveWorkOrder(workOrder, serviceProviderLoggedIn.user.license)) match
      case workOrderSaved: WorkOrderSaved => workOrderSaved.success shouldBe true
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

    println("*** sending emails ...")
    Thread.sleep(6000)
    println("*** integration test complete!")
  }