package poolmate

final class Service(store: Store):
  def register(register: Register): Registered = ???

  def login(login: Login): LoggedIn = ???

  def saveUser(saveUser: SaveUser): UserSaved = ???

  def saveWorkOrder(saveWorkOrder: SaveWorkOrder): WorkOrderSaved = ???
  
  def listWorkOrders(listWorkOrders: ListWorkOrders): WorkOrdersListed = ???