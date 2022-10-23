package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object WorkOrderView extends View:
  def apply(): HtmlElement =
    val streetAddressErrorBus = new EventBus[String]

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
      err(errorBus),
      lbl("Street Address"),
      email.amend {
        onInput.mapToValue.filter(_.nonEmpty) --> { streetAddress =>
          Model.workOrderVar.update(workOrder => workOrder.copy(streetAddress = streetAddress))
        }
        onKeyUp.mapToValue --> { value =>
          if value.isStreetAddress then clear(streetAddressErrorBus)
          else emit(streetAddressErrorBus, streetAddressInvalid)
        }
      },
      err(streetAddressErrorBus),
      cbar(
        btn("Save")
      )
    )
