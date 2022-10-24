package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object WorkOrderView extends View:
  def apply(): HtmlElement =
    val titleErrorBus = new EventBus[String]
    val issueEventBus = new EventBus[String]
    val streetAddressErrorBus = new EventBus[String]
    val imageUrlErrorBus = new EventBus[String]
    val resolutionErrorBus = new EventBus[String]
    val openedErrorBus = new EventBus[String]
    val closedErrorBus = new EventBus[String]

    def handler(either: Either[Fault, Event]): Unit =
      either match
        case Left(fault) => errorBus.emit(s"Save work order failed: ${fault.cause}")
        case Right(event) =>
          event match
            case WorkOrderAdded(_, _, _) | WorkOrderSaved(_, _, _) =>
              clearErrorBus()
              route(WorkOrdersPage)
            case _ => log("work order view: handler failed: %o", event)
    // TODO role? add? edit? readonly?
    div(
      bar(
        btn("Work Orders").amend {
          onClick --> { _ => route(WorkOrdersPage) }
        }      
      ),
      hdr("Work Order"),
      err(errorBus),
      lbl("Number"),
      rotxt.amend {
        value <-- Model.workOrderVar.signal.map(_.number.toString)
      },
      lbl("Title"),
      txt.amend {
        onInput.mapToValue.filter(_.nonEmpty) --> { value =>
          Model.workOrderVar.update(workOrder => workOrder.copy(title = value))
        }
        onKeyUp.mapToValue --> { value =>
          if value.isTitle then clear(titleErrorBus)
          else emit(titleErrorBus, titleInvalid)
        }
      },
      err(titleErrorBus),
      lbl("Street Address"),
      street.amend {
        onInput.mapToValue.filter(_.nonEmpty) --> { value =>
          Model.workOrderVar.update(workOrder => workOrder.copy(streetAddress = value))
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
          onClick --> { _ => // TODO Add or Save
            val command = SaveWorkOrder(Model.workOrderVar.now(), Model.userVar.now().license)
            log("work order view: save button onClick command: %o", command)
            call(command, handler)
          }
        })
    )
