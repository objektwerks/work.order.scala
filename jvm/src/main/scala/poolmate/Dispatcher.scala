package poolmate

final class Dispatcher(emailer: Emailer,
                       service: Service):
  def dispatch(command: Command): Event =
    command match
      case register: Register =>
        service.register(register)
      
      case login: Login =>
        service.login(login)
      
      case saveUser: SaveUser =>
        service.saveUser(saveUser)
      
      case saveWorkOrder: SaveWorkOrder =>
        service.saveWorkOrder(saveWorkOrder)
      
      case listWorkOrders: ListWorkOrders =>
        service.listWorkOrders(listWorkOrders)