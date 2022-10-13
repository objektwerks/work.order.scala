package poolmate

import com.raquo.laminar.api.L.*

sealed trait Page:
  val title = "Work Order"

case object RootPage extends Page
case object RegisterPage extends Page
case object LoginPage extends Page

case object UserPage extends Page
case object WorkOrdersPage extends Page