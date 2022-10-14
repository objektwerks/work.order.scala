package poolmate

import com.typesafe.scalalogging.LazyLogging

import scala.util.Try
import fansi.ErrorMode.Throw

final class Service(emailer: Emailer, store: Store) extends LazyLogging:
  val subjectRegistration = "Work Order Registration"
  val subjectNotification = "Work Order Notification"

  def log(method: String, message: String): Unit = logger.info(s"*** $method: $message")

  def logError(method: String, message:  String): Unit = logger.error(s"*** $method error: $message")

  def isLicenseValid(license: String): Boolean = store.isLicenseValid(license)

  def register(register: Register): Registered =
    val pin = User.newPin
    Try {
      var user = new User(0, register.role, register.name, register.emailAddress, register.streetAddress, "", pin, "")
      val html = "<p>Your new 7-character pin is: <b>${pin}</b> Use it to login. Print this email, keep it in a safe place and <b>delete it!</b></p>"
      emailer.send(List(register.emailAddress), subjectRegistration, html)
      user = store.addUser(user)
      log("register", "succeeded for: ${register.emailAddress}")
      Registered.success(pin)
    }.recover { case error =>
      logError("register", s"for: ${register.emailAddress} because: ${error.getMessage()}")
      Registered.fail(s"Register failed for: ${register.emailAddress}")
    }.get

  def login(login: Login): LoggedIn = LoggedIn.success(User.empty, Array.empty[User], Array.empty[WorkOrder])

  def saveUser(saveUser: SaveUser): UserSaved = UserSaved.success(saveUser.user.id)

  def addWorkOrder(addWorkOrder: AddWorkOrder): WorkOrderAdded = WorkOrderAdded.success(addWorkOrder.workOrder.number)

  def saveWorkOrder(saveWorkOrder: SaveWorkOrder): WorkOrderSaved = WorkOrderSaved.success(saveWorkOrder.workOrder.number)

  def listWorkOrders(listWorkOrders: ListWorkOrders): WorkOrdersListed = WorkOrdersListed.success(listWorkOrders.userId, Array.empty[WorkOrder])