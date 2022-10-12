package poolmate

object Serializers:
  import upickle.default.*

  given userRW: ReadWriter[User] = macroRW
  given workOrderRW: ReadWriter[WorkOrder] = macroRW
  given entityRW: ReadWriter[Entity] = ReadWriter.merge( userRW, workOrderRW )

  given registerRW: ReadWriter[Register] = macroRW
  given loginRW: ReadWriter[Login] = macroRW
  given saveUserRW: ReadWriter[SaveUser] = macroRW
  given saveWorkOrderRW: ReadWriter[SaveWorkOrder] = macroRW
  given listWorkOrdersRW: ReadWriter[ListWorkOrders] = macroRW
  given commandRW: ReadWriter[Command] = ReadWriter.merge( registerRW, loginRW, saveUserRW, saveWorkOrderRW, listWorkOrdersRW )