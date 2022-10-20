package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object AppMenu extends View:
  def apply(): HtmlElement =
    div(
      bar(
        btn("Profile").amend {
          onClick --> { _ =>
            log("App -> Profile menu item onClick")
            route(ProfilePage)
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