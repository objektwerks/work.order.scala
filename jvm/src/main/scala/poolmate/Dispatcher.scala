package poolmate

import Validator.*

final class Dispatcher(emailer: Emailer, service: Service):
  def dispatch(command: Command): Event =
    command match
      case register: Register =>
        if register.isValid then service.register(register)
        else Registered.fail("Register invalid.")
      
      case login: Login =>
        if login.isValid then service.login(login)
        else LoggedIn.fail("Login invalid.")
      
      case saveUser: SaveUser =>
        val user = saveUser.user
        val license = saveUser.user.license
        if license.isLicense && service.isLicenseValid(license) then
          if user.isValid then service.saveUser(saveUser)
          else UserSaved.fail(user.id, "User invalid.")
        else UserSaved.fail(user.id, "License invalid: ${saveUser.user.license}")
      
      case saveWorkOrder: SaveWorkOrder =>
        service.saveWorkOrder(saveWorkOrder)
      
      case listWorkOrders: ListWorkOrders =>
        service.listWorkOrders(listWorkOrders)