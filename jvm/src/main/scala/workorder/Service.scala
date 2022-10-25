package workorder

import com.typesafe.scalalogging.LazyLogging

import scala.util.Try

final class Service(emailer: Emailer, store: Store) extends LazyLogging:
  private val subjectRegistration = "Work Order Registration"
  private val subjectNotification = "Work Order Notification"

  private def log(method: String, message: String): Unit = logger.info(s"*** $method: $message")

  private def logError(method: String, message:  String): Unit = logger.error(s"*** $method error: $message")

  def isLicenseValid(license: String): Boolean = store.isLicenseValid(license)

  def register(register: Register): Registered =
    Try {
      val pin = User.newPin
      var user = User(0, register.role, register.name, register.emailAddress, register.streetAddress, DateTime.now, pin, "")
      val html = s"<p>Your new 7-character pin is: <b>$pin</b> Use it to login. Print this email, keep it in a safe place and <b>delete it!</b></p>"
      if emailer.send(List(register.emailAddress), subjectRegistration, html).isSuccess then
        user = store.addUser(user)
        log("register", s"succeeded for: ${register.emailAddress}")
        Registered.success(pin)
      else
        logError("register", s"failed for: ${register.emailAddress}")
        Registered.fail(s"Register failed for: ${register.emailAddress}")
    }.recover { case error =>
      logError("register", s"failed for: ${register.emailAddress} because: ${error.getMessage}")
      Registered.fail(s"Register failed for: ${register.emailAddress}")
    }.get

  /**
   *  user.role == service provider:
   *    don't list service providers
   *    list all homeowners in assigned work orders to a single service provider
   *  user.role == homeowner:
   *    don't list homeowners
   *    list all service providers
   */
  def login(login: Login): LoggedIn =
    Try {
      val user = store.getUserByEmailAddressPin(login.emailAddress, login.pin).get
      val users = user.role match
        case Roles.serviceProvider => store.listHomeownersInWorkOrdersByServiceProviderId(user.id)
        case Roles.homeowner => store.listServiceProviders()
      val workOrders = store.listWorkOrders(user.id)
      log("login", s"succeeded for: ${login.emailAddress}")
      LoggedIn.success(user, users, workOrders)
    }.recover { case error =>
      logError("login", s"failed error: $error for: ${login.emailAddress}")
      LoggedIn.fail(s"Login failed for: ${login.emailAddress}")
    }.get

  def saveUser(saveUser: SaveUser): UserSaved =
    val user = saveUser.user
    Try {
      store.saveUser(user)
      log("saveUser", s"succeeded for user id: ${user.id}")
      UserSaved.success(user.id)
    }.recover { error =>
      logError("saveUser", s"failed error: $error for: $user")
      UserSaved.fail(user.id, "Save user failed.")
    }.get

  def addWorkOrder(addWorkOrder: AddWorkOrder): WorkOrderAdded =
    Try {
      val workOrder = store.addWorkOrder(addWorkOrder.workOrder)
      log("addWorkOrder", s"succeeded for number: ${workOrder.number}")
      val recipients = store.listEmailAddressesByIds(workOrder.homeownerId, workOrder.serviceProviderId)
      val html = s"<p>A new work order, number <b>${workOrder.number}</b>, has been opened.</p>"
      if emailer.send(recipients, subjectNotification, html).isSuccess then WorkOrderAdded.success(workOrder.number)
      else WorkOrderAdded.fail("add work order failed")
    }.recover { case _ =>
      logError("addWorkOrder", "failed error: ${error} for: ${saveWorkOrder}")
      WorkOrderAdded.fail("Add work order failed.")
    }.get

  def saveWorkOrder(saveWorkOrder: SaveWorkOrder): WorkOrderSaved =
    val workOrder = saveWorkOrder.workOrder
    Try {
      store.saveWorkOrder(workOrder)
      log("saveWorkOrder", s"succeeded for number: ${workOrder.number}")
      val recipients = store.listEmailAddressesByIds(workOrder.homeownerId, workOrder.serviceProviderId)
      val updatedOrClosed = if workOrder.closed.isEmpty then "updated" else "closed"
      val html = s"<p>Open work order, number <b>${saveWorkOrder.workOrder.number}</b>, has been $updatedOrClosed.</p>"
      if emailer.send(recipients, subjectNotification, html).isSuccess then WorkOrderSaved.success(workOrder.number)
      else WorkOrderSaved.fail(workOrder.number, "Save work order failed")
    }.recover { case _ =>
      logError("saveWorkOrder", "failed error: ${error} for: ${saveWorkOrder}")
      WorkOrderSaved.fail(workOrder.number, "Save work order failed.")
    }.get
  
  def listWorkOrders(listWorkOrders: ListWorkOrders): WorkOrdersListed =
    val userId = listWorkOrders.userId
    Try {
      val workOrders = store.listWorkOrders(userId)
      log("listWorkOrders", s"succeeded for user id: $userId")
      WorkOrdersListed.success(userId, workOrders)
    }.recover { case error =>
      logError("listWorkOrders", s"failed error: $error for user id: $userId")
      WorkOrdersListed.fail(userId, "List work orders failed.")
    }.get