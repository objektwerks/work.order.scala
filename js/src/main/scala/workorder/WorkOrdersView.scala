package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object WorkOrdersView extends View:
  def apply(): HtmlElement =
    def handler(either: Either[Fault, Event]): Unit =
      either match
        case Left(fault) => errorBus.emit(s"Refresh work orders failed: ${fault.cause}")
        case Right(event) =>
          event match
            case WorkOrdersListed(_, workOrders: List[WorkOrder], _, _) =>
              clearErrorBus()
              Model.workOrdersVar.set(workOrders)
            case _ => log(s"ork orders view: handler failed: $event")

    div(
      bar(
        btn("Profile").amend {
          onClick --> { _ =>
            log("work orders view: profile menu item onClick")
            route(ProfilePage)
          }
        }      
      ),
      div(
        hdr("Work Orders"),
        lbl("Open"),
        list(listWorkOrders(Model.openWorkOrders)),
        lbl("Closed"),
        list(listWorkOrders(Model.closedWorkOrders)),
        cbar(
          btn("New").amend {
            onClick --> { _ =>
              log("work ordesr view: new button onClick")
              route(WorkOrderPage)
            }
          }),
          btn("Refresh").amend {
            onClick --> { _ =>
              log("work ordesr view: refresh button onClick")
              // TODO
            }
          }),
        )