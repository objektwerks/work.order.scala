package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object WorkOrdersView extends View:
  def apply(workOrdersVar: Var[Array[WorkOrder]]): HtmlElement =
    div(
      bar(
        btn("App").amend {
          onClick --> { _ =>
            log("Account -> App menu item onClick")
            route(RootPage)
          }
        }      
      ),
      div(
        hdr("Work Orders"),
        // List of opened and closed work orders.
        cbar(
          btn("New"),
          btn("Refresh")
        )
      )
    )