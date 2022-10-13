package poolmate

final class Dispatcher(emailer: Emailer,
                       service: Service):
  def dispatch(command: Command): Event =
    command match
      case register: Register => service.register(register)
      case login: Login => service.login(login)
      case saveUser: SaveUser =>
      case saveWorkOrder: SaveWorkOrder =>
      case listWorkOrders: ListWorkOrders =>