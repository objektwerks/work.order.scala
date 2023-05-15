package workorder

import com.raquo.laminar.api.L.*

import upickle.default.*

object Page:
  sealed trait Page:
    val title = "Work Order"

  case class LoginRegisterPage() extends Page
  case class RegisterPage() extends Page
  case class LoginPage() extends Page
  case class ProfilePage() extends Page
  case class WorkOrdersPage() extends Page
  case class WorkOrderPage() extends Page
  
  given LoginRegisterPageRW: ReadWriter[LoginRegisterPage] = macroRW
  given RegisterPageRW: ReadWriter[RegisterPage] = macroRW
  given LoginPageRW: ReadWriter[LoginPage] = macroRW
  given ProfilePageRW: ReadWriter[ProfilePage] = macroRW
  given WorkOrdersPageRW: ReadWriter[WorkOrdersPage] = macroRW
  given WorkOrderPageRW: ReadWriter[WorkOrderPage] = macroRW
  given pageRW: ReadWriter[Page] = ReadWriter.merge(
    LoginRegisterPageRW, RegisterPageRW, LoginPageRW, ProfilePageRW, WorkOrdersPageRW, WorkOrderPageRW
  )