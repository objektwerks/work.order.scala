package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object WorkOrderView extends View:
  def apply(): HtmlElement =
    div(
      bar(
        btn("Work Orders").amend {
          onClick --> { _ =>
            log("work order view: work orders menu item onClick")
            route(WorkOrdersPage)
          }
        }      
      ),
      hdr("Work Order"),
      // work order
      cbar(
        btn("Save")
      )
    )
