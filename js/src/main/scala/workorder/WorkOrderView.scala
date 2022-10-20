package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object WorkOrderView extends View:
  def apply(workOrderVar: Var[WorkOrder]): HtmlElement =
    div(
      div(
        hdr("Work Orders"),
        // work order
        cbar(
          btn("Save")
        )
      )
    )