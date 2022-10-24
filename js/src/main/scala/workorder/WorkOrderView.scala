package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object WorkOrderView extends View:
  def apply(): HtmlElement =
    val streetAddressErrorBus = new EventBus[String]

    def handler(either: Either[Fault, Event]): Unit =
      either match
        case Left(fault) => errorBus.emit(s"Save work order failed: ${fault.cause}")
        case Right(event) =>
          event match
            case WorkOrderAdded(_, _, _) | WorkOrderSaved(_, _, _) =>
              clearErrorBus()
              route(WorkOrdersPage)
            case _ => log("work order view: handler failed: %o", event)
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
      street.amend {
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
        btn("Save").amend {
          disabled <-- Model.workOrderVar.signal.map { workOrder => !workOrder.isValid }
          onClick --> { _ =>
            val command = SaveWorkOrder(Model.workOrderVar.now(), Model.userVar.now().license)
            log("work order view: save button onClick command: %o", command)
            call(command, handler)
          }
        })
    )
