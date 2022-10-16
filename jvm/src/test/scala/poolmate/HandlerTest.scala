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
    val serviceProviderEmail = conf.getString("email.serviceProviderEmail")
    val homeownerEmail = conf.getString("email.homeownerEmail")

    // register
    val serviceProviderRegistered = handler.register(Register(Roles.serviceProvider, "lawncare service", serviceProviderEmail, "123 green rd"))
    val homeownerRegistered = handler.register(Register(Roles.homeowner, "fred flintstone", homeownerEmail, "345 stone st"))
    assert(serviceProviderRegistered.success, "*** register service provider failed: ${toJson(serviceProviderRegistered)}")
    assert(homeownerRegistered.success, "*** register homeowner failed: ${toJson(homeownerRegistered)}")

    // login
    val serviceProviderLoggedIn = handler.login(Login(serviceProviderEmail, serviceProviderRegistered.pin))
    val homeownerLoggedIn =  handler.login(new Login(homeownerEmail, homeownerRegistered.pin))
    assert(serviceProviderLoggedIn.success, "*** login service provider failed: ${toJson(serviceProviderLoggedIn)}")
    assert(serviceProviderLoggedIn.user.pin === serviceProviderRegistered.pin, "*** logged in service provider pin is invalid: ${serviceProviderRegistered.pin}")
    assert(homeownerLoggedIn.success, "*** login homeowner failed: ${toJson(homeownerLoggedIn)}")
    assert(homeownerLoggedIn.user.pin === homeownerRegistered.pin, "*** logged in homeowner pin is invalid: ${homeownerRegistered.pin}")

    // work order add
    val workOrder = new WorkOrder(0, homeownerLoggedIn.user.id, serviceProviderLoggedIn.user.id, "sprinkler", "345 stone st", "broken", "", "", new Date().toISOString(), "")
    val workOrderAdded =  handler.addWorkOrder(new SaveWorkOrder(workOrder, homeownerLoggedIn.user.license))
    workOrder.number = workOrderAdded.number
    assert(workOrderAdded.success, "*** add work order failed: ${toJson(workOrderAdded)}")
    assert(workOrderAdded.number > 0, "*** work order number invalid: ${workOrderAdded.number}")

    // work order save
    workOrder.resolution = "fixed"
    workOrder.closed = new Date().toISOString()
    val workOrderSaved =  handler.saveWorkOrder(new SaveWorkOrder(workOrder, homeownerLoggedIn.user.license))
    assert(workOrderSaved.success, "*** save work order failed: ${toJson(workOrderSaved)}")

    // user save
    val serviceProviderUserSaved =  handler.saveUser(new SaveUser(serviceProviderLoggedIn.user))
    val homeownerUserSaved =  handler.saveUser(new SaveUser(homeownerLoggedIn.user))
    assert(serviceProviderUserSaved.success, "*** save service provider user failed: ${serviceProviderUserSaved}")
    assert(homeownerUserSaved.success, "*** save homeowner user failed: ${toJson(homeownerUserSaved)}")

    // work orders list
    val workOrdersListed =  handler.listWorkOrders(new ListWorkOrders(homeownerLoggedIn.user.id, homeownerLoggedIn.user.license))
    assert(workOrdersListed.success, "*** list work orders failed: ${toJson(workOrdersListed)}")
    assert(workOrdersListed.workOrders.length === 1, "*** list work orders size !== 1")

  }