package poolmate

sealed trait Event

final case class Registered(pin: String,
                            success: Boolean = true,
                            error: String = "") extends Event:
  def success(pin: String): Registered = Registered(pin)
  def fail(error: String): Registered = Registered("", false, error)

final case class LoggedIn(user: User, 
                          serviceProviders: Array[User], 
                          workOrders: Array[WorkOrder], 
                          success: Boolean = true, 
                          error: String = "") extends Event:
  def success(user: User, serviceProviders: Array[User], workOrders: Array[WorkOrder]): LoggedIn = LoggedIn(user, serviceProviders, workOrders)
  def fail(error: String): LoggedIn = LoggedIn(User.empty, Array.empty[User], Array.empty[WorkOrder], false, error)

final case class UserSaved(id: Int, 
                           success: Boolean = true, 
                           error: String = "") extends Event:
  def success(id: Int): UserSaved = UserSaved(id)
  def fail(id: Int, error: String): UserSaved = UserSaved(id, false, error)

final case class WorkOrderSaved(number: Int,
                                success: Boolean = true,
                                error: String = "") extends Event:
  def success(number: Int): WorkOrderSaved = WorkOrderSaved(number)
  def fail(number: Int, error: String): WorkOrderSaved = WorkOrderSaved(number, false, error)

final case class WorkOrdersListed(userId: Int, 
                                  workOrders: Array[WorkOrder], 
                                  success: Boolean = true, 
                                  error: String = "") extends Event:
  def success(userId: Int, workOrders: Array[WorkOrder]): WorkOrdersListed = WorkOrdersListed(userId, workOrders)
  def fail(userId: Int, error: String): WorkOrdersListed = WorkOrdersListed(userId, Array.empty[WorkOrder], false, error)