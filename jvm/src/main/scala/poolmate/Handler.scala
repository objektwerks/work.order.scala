package poolmate

import Validator.*

final class Handler(service: Service):
  def handle(command: Command): Event =
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
        else UserSaved.fail(user.id, s"License invalid: $license")

      case addWorkOrder: AddWorkOrder =>
        val workOrder = addWorkOrder.workOrder
        val license = addWorkOrder.license
        if license.isLicense && service.isLicenseValid(license) then
          if workOrder.isValid then service.addWorkOrder(addWorkOrder)
          else WorkOrderAdded.fail("Work order invalid.")
        else WorkOrderAdded.fail(s"License invalid: $license")
      
      case saveWorkOrder: SaveWorkOrder =>
        val workOrder = saveWorkOrder.workOrder
        val license = saveWorkOrder.license
        if license.isLicense && service.isLicenseValid(license) then
          if workOrder.isValid then service.saveWorkOrder(saveWorkOrder)
          else WorkOrderSaved.fail(workOrder.number, "Work order invalid.")
        else WorkOrderSaved.fail(workOrder.number, s"License invalid: $license")

      case listWorkOrders: ListWorkOrders =>
        val userId = listWorkOrders.userId
        val license = listWorkOrders.license
        if license.isLicense && service.isLicenseValid(license) then
          if userId.isGreaterThanZero then service.listWorkOrders(listWorkOrders)
          else WorkOrdersListed.fail(userId, "User id invalid.")
        else WorkOrdersListed.fail(userId, s"License invalid: $license")