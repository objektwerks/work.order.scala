package poolmate

import Validator.*

final class Dispatcher(emailer: Emailer, service: Service):
  def dispatch(command: Command): Event =
    command match
      case register: Register =>
        if register.isValid then service.register(register)
        else Registered.fail("Register is invalid.")
      
      case login: Login =>
        if login.isValid then service.login(login)
        else LoggedIn.fail("Login is invalid.")
      
      case saveUser: SaveUser =>
        service.saveUser(saveUser)
      
      case saveWorkOrder: SaveWorkOrder =>
        service.saveWorkOrder(saveWorkOrder)
      
      case listWorkOrders: ListWorkOrders =>
        service.listWorkOrders(listWorkOrders)