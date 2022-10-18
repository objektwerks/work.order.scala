package workorder

import Validator.*

final class Handler(service: Service):
  def register(register: Register): Registered =
    if register.isValid then service.register(register)
    else Registered.fail("Register invalid.")
  
  def login(login: Login): LoggedIn =
    if login.isValid then service.login(login)
    else LoggedIn.fail("Login invalid.")
  
  def saveUser(saveUser: SaveUser): UserSaved =
    val user = saveUser.user
    val license = saveUser.user.license
    if license.isLicense && service.isLicenseValid(license) then
      if user.isValid then service.saveUser(saveUser)
      else UserSaved.fail(user.id, "User invalid.")
    else UserSaved.fail(user.id, s"License invalid: $license")

  def addWorkOrder(addWorkOrder: AddWorkOrder): WorkOrderAdded =
    val workOrder = addWorkOrder.workOrder
    val license = addWorkOrder.license
    if license.isLicense && service.isLicenseValid(license) then
      if workOrder.isValid then service.addWorkOrder(addWorkOrder)
      else WorkOrderAdded.fail("Work order invalid.")
    else WorkOrderAdded.fail(s"License invalid: $license")
  
  def saveWorkOrder(saveWorkOrder: SaveWorkOrder): WorkOrderSaved =
    val workOrder = saveWorkOrder.workOrder
    val license = saveWorkOrder.license
    if license.isLicense && service.isLicenseValid(license) then
      if workOrder.isValid then service.saveWorkOrder(saveWorkOrder)
      else WorkOrderSaved.fail(workOrder.number, "Work order invalid.")
    else WorkOrderSaved.fail(workOrder.number, s"License invalid: $license")

  def listWorkOrders(listWorkOrders: ListWorkOrders): WorkOrdersListed =
    val userId = listWorkOrders.userId
    val license = listWorkOrders.license
    if license.isLicense && service.isLicenseValid(license) then
      if userId.isGreaterThanZero then service.listWorkOrders(listWorkOrders)
      else WorkOrdersListed.fail(userId, "User id invalid.")
    else WorkOrdersListed.fail(userId, s"License invalid: $license")