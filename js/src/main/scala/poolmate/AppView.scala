package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object AppView extends View:
  def apply(): HtmlElement =
    div(
      bar(
        btn("User").amend {
          onClick --> { _ =>
            log("App -> User menu item onClick")
            route(UserPage)
          }
        },
        rbtn("Work Orders").amend {
          onClick --> { _ =>
            log("App -> Workorders menu item onClick")
            route(WorkOrdersPage)
          }
        }          
      )
    )