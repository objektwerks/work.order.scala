package poolmate

import upickle.default.*

object Serializers:
  given userRW: ReadWriter[User] = macroRW
  given workOrderRW: ReadWriter[WorkOrder] = macroRW
  given entityRW: ReadWriter[Entity] = ReadWriter.merge( userRW, workOrderRW )

  given registerRW: ReadWriter[Register] = macroRW
  given loginRW: ReadWriter[Login] = macroRW
  given saveUserRW: ReadWriter[SaveUser] = macroRW
  given saveWorkOrderRW: ReadWriter[SaveWorkOrder] = macroRW
  given listWorkOrdersRW: ReadWriter[ListWorkOrders] = macroRW
  given commandRW: ReadWriter[Command] = ReadWriter.merge( registerRW, loginRW, saveUserRW, saveWorkOrderRW, listWorkOrdersRW )

  given registeredRW: ReadWriter[Registered] = macroRW
  given loggedInRW: ReadWriter[LoggedIn] = macroRW
  given userSavedRW: ReadWriter[UserSaved] = macroRW
  given workOrderSavedRW: ReadWriter[WorkOrderSaved] = macroRW
  given workOrdersListedRW: ReadWriter[WorkOrdersListed] = macroRW
  given eventRW: ReadWriter[Event] = ReadWriter.merge( registeredRW, loggedInRW, userSavedRW, workOrderSavedRW, workOrdersListedRW )