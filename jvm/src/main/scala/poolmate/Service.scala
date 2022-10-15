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

  def login(login: Login): LoggedIn = LoggedIn.success(User.empty, Array.empty[User], Array.empty[WorkOrder])

  def saveUser(saveUser: SaveUser): UserSaved = UserSaved.success(saveUser.user.id)

  def addWorkOrder(addWorkOrder: AddWorkOrder): WorkOrderAdded = WorkOrderAdded.success(addWorkOrder.workOrder.number)

  def saveWorkOrder(saveWorkOrder: SaveWorkOrder): WorkOrderSaved = WorkOrderSaved.success(saveWorkOrder.workOrder.number)

  def listWorkOrders(listWorkOrders: ListWorkOrders): WorkOrdersListed = WorkOrdersListed.success(listWorkOrders.userId, Array.empty[WorkOrder])