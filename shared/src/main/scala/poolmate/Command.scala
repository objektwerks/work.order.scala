package poolmate

sealed trait Command

final case class Register(role: String,
                          name: String,
                          emailAddress: String,
                          streetAddress: String)

final case class Login(emailAddress: String, pin: String) extends Command

final case class SaveUser(user: User) extends Command

final case class SaveWorkOrder(workOrder: WorkOrder, license: String) extends Command

final case class ListWorkOrders(userId: Int, license: String) extends Command