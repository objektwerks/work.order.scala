package workorder

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
    println("*** handler integration test running...")

    val serviceProviderEmail = conf.getString("email.serviceProviderEmail")
    val homeownerEmail = conf.getString("email.homeownerEmail")

    // register
    val serviceProviderRegister = Register(Roles.serviceProvider, "lawncare service", serviceProviderEmail, "123 green rd")
    val serviceProviderRegistered = handler.register(serviceProviderRegister)
    serviceProviderRegistered.success shouldBe true

    val homeownerRegister = Register(Roles.homeowner, "fred flintstone", homeownerEmail, "345 stone st")
    val homeownerRegistered = handler.register(homeownerRegister)
    homeownerRegistered.success shouldBe true

    // login
    val serviceProviderLoggedIn = handler.login(Login(serviceProviderEmail, serviceProviderRegistered.pin))
    serviceProviderLoggedIn.success shouldBe true
    
    val homeownerLoggedIn = handler.login(Login(homeownerEmail, homeownerRegistered.pin))
    homeownerLoggedIn.success shouldBe true

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
    val workOrderAdded = handler.addWorkOrder(AddWorkOrder(workOrder, homeownerLoggedIn.user.license))
    workOrderAdded.success shouldBe true

    // work order save
    workOrder = workOrder.copy(number = workOrderAdded.number, resolution = "fixed", closed = DateTime.now)
    val workOrderSaved = handler.saveWorkOrder(SaveWorkOrder(workOrder, serviceProviderLoggedIn.user.license))
    workOrderSaved.success shouldBe true
    
    // user save
    val serviceProviderSaved = handler.saveUser(SaveUser(serviceProviderLoggedIn.user))
    serviceProviderSaved.success shouldBe true

    val homeownerSaved = handler.saveUser(SaveUser(homeownerLoggedIn.user))
    homeownerSaved.success shouldBe true

    // work orders list
    val workOrdersListed = handler.listWorkOrders(ListWorkOrders(homeownerLoggedIn.user.id, homeownerLoggedIn.user.license))
    workOrdersListed.success shouldBe true
    workOrdersListed.workOrders.size shouldBe 1

    println("*** handler integration test complete!")
  }