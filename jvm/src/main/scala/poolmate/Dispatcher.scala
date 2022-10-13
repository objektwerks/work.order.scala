package poolmate

import Validator.*

final class Dispatcher(emailer: Emailer, service: Service):
  def dispatch(command: Command): Event =
    command match
      case register: Register =>
        if register.isValid then service.register(register)
        else Registered.fail("Register is invalid.")
      
      case login: Login =>
        service.login(login)
      
      case saveUser: SaveUser =>
        service.saveUser(saveUser)
      
      case saveWorkOrder: SaveWorkOrder =>
        service.saveWorkOrder(saveWorkOrder)
      
      case listWorkOrders: ListWorkOrders =>
        service.listWorkOrders(listWorkOrders)