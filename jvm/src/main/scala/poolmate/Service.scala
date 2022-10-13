package poolmate

final class Service(store: Store):
  def isLicenseValid(license: String): Boolean = store.isLicenseValid(license)

  def register(register: Register): Registered = Registered.success(User.newPin)

  def login(login: Login): LoggedIn = LoggedIn.success(User.empty, Array.empty[User], Array.empty[WorkOrder])

  def saveUser(saveUser: SaveUser): UserSaved = UserSaved.success(saveUser.user.id)

  def addWorkOrder(addWorkOrder: AddWorkOrder): WorkOrderAdded = WorkOrderAdded.success(addWorkOrder.workOrder.number)

  def saveWorkOrder(saveWorkOrder: SaveWorkOrder): WorkOrderSaved = WorkOrderSaved.success(saveWorkOrder.workOrder.number)

  def listWorkOrders(listWorkOrders: ListWorkOrders): WorkOrdersListed = WorkOrdersListed.success(listWorkOrders.userId, Array.empty[WorkOrder])