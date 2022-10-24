package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object WorkOrdersView extends View:
  def apply(): HtmlElement =
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
              // TODO
            }
          }),
          btn("Refresh").amend {
            onClick --> { _ =>
              log("work ordesr view: refresh button onClick")
              // TODO
            }
          }),
        )