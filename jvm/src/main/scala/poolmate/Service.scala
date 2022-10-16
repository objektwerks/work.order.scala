package poolmate

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
      var user = new User(0, register.role, register.name, register.emailAddress, register.streetAddress, "", pin, "")
      val html = s"<p>Your new 7-character pin is: <b>$pin</b> Use it to login. Print this email, keep it in a safe place and <b>delete it!</b></p>"
      if emailer.send(List(register.emailAddress), subjectRegistration, html).isSuccess then
        user = store.addUser(user)
        log("register", s"succeeded for: ${register.emailAddress}")
        Registered.success(pin)
      else
        logError("register", s"failed for: ${register.emailAddress}")
        Registered.fail(s"Register failed for: ${register.emailAddress}")
    }.recover { case error =>
      logError("register", s"failed for: ${register.emailAddress} because: ${error.getMessage()}")
      Registered.fail(s"Register failed for: ${register.emailAddress}")
    }.get

  def login(login: Login): LoggedIn =
    Try {
      val user = store.getUserByEmailAddressPin(login.emailAddress, login.pin)
      val serviceProviders = store.listUsersByRole(Roles.serviceProvider)
      val workOrders = store.listWorkOrders(user.get.id)
      log("login", s"succeeded for: ${login.emailAddress}")
      LoggedIn.success(user.get, serviceProviders, workOrders)
    }.recover { case error =>
      logError("login", s"failed error: ${error} for: ${login.emailAddress}")
      LoggedIn.fail(s"Login failed for: ${login.emailAddress}")
    }.get

  def saveUser(saveUser: SaveUser): UserSaved =
    val user = saveUser.user
    Try {
      store.saveUser(user)
      log("saveUser", s"succeeded for user id: ${user.id}")
      UserSaved.success(user.id)
    }.recover { error =>
      logError("saveUser", s"failed error: ${error} for: ${user}")
      UserSaved.fail(user.id, "Save user failed.")
    }.get

  def addWorkOrder(addWorkOrder: AddWorkOrder): WorkOrderAdded =
    var workOrder = addWorkOrder.workOrder
    Try {
      workOrder = store.addWorkOrder(workOrder)
      log("addWorkOrder", s"succeeded for number: ${workOrder.number}")
      val recipients = store.listEmailAddressesByIds(workOrder.homeownerId, workOrder.serviceProviderId)
      val html = s"<p>A new work order, number <b>${workOrder.number}</b>, has been opened.</p>"
      if emailer.send(recipients, subjectNotification, html).isSuccess then WorkOrderAdded.success(workOrder.number)
      else WorkOrderAdded.fail("add work order failed")
    }.recover { case error =>
      logError("addWorkOrder", "failed error: ${error} for: ${saveWorkOrder}")
      WorkOrderAdded.fail("Add work order failed.")
    }.get

  def saveWorkOrder(saveWorkOrder: SaveWorkOrder): WorkOrderSaved =
    val workOrder = saveWorkOrder.workOrder
    Try {
      store.saveWorkOrder(workOrder)
      log("saveWorkOrder", s"succeeded for number: ${workOrder.number}")
      val recipients = store.listEmailAddressesByIds(workOrder.homeownerId, workOrder.serviceProviderId)
      val updatedOrClosed = if workOrder.closed.length == 0 then "updated" else "closed"
      val html = s"<p>Open work order, number <b>${saveWorkOrder.workOrder.number}</b>, has been ${updatedOrClosed}.</p>"
      if emailer.send(recipients, subjectNotification, html).isSuccess then WorkOrderSaved.success(workOrder.number)
      else WorkOrderSaved.fail(workOrder.number, "Save work order failed")
    }.recover { case error =>
      logError("saveWorkOrder", "failed error: ${error} for: ${saveWorkOrder}")
      WorkOrderSaved.fail(workOrder.number, "Save work order failed.")
    }.get
  
  def listWorkOrders(listWorkOrders: ListWorkOrders): WorkOrdersListed =
    WorkOrdersListed.success(listWorkOrders.userId, List.empty[WorkOrder])