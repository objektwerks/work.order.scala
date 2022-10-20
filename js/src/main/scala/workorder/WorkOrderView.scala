package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object WorkOrderView extends View:
  def apply(workOrderVar: Var[WorkOrder]): HtmlElement =
    div(
      bar(
        btn("App").amend {
          onClick --> { _ =>
            log("WorkOrder -> App menu item onClick")
            route(AppPage)
          }
        }      
      ),
      hdr("Work Orders"),
      // work order
      cbar(
        btn("Save")
      )
    )
