package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object ProfileView extends View:
  def apply(): HtmlElement =
    val emailAddressErrorBus = new EventBus[String]
    val streetAddressErrorBus = new EventBus[String]

    def handler(either: Either[Fault, Event]): Unit =
      either match
        case Left(fault) => errorBus.emit(s"Save profile failed: ${fault.cause}")
        case Right(event) =>
          event match
            case UserSaved(_, _, _) =>
              clearErrorBus()
              route(WorkOrdersPage)
            case _ => log("profile view: handler failed: %o", event)

    div(
      bar(
        btn("Work Orders").amend {
          onClick --> { _ =>
            log("profile view: work orders menu item onClick")
            route(WorkOrdersPage)
          }
        }      
      ),
      div(
        hdr("Profile"),
        err(errorBus),
        lbl("Role"),
        rotxt.amend {
          value <-- Model.userVar.signal.map(_.role)
        },
        lbl("Name"),
        rotxt.amend {
          value <-- Model.userVar.signal.map(_.name)
        },
        lbl("Email Address"),
        email.amend {
          onInput.mapToValue.filter(_.nonEmpty) --> { emailAddress =>
            Model.userVar.update(user => user.copy(emailAddress = emailAddress))
          }
          onKeyUp.mapToValue --> { value =>
            if value.isEmailAddress then clear(emailAddressErrorBus)
            else emit(emailAddressErrorBus, emailAddressInvalid)
          }
        },
        err(emailAddressErrorBus),
        lbl("Street Address"),
        street.amend {
          onInput.mapToValue.filter(_.nonEmpty) --> { streetAddress =>
            Model.userVar.update(user => user.copy(streetAddress = streetAddress))
          }
          onKeyUp.mapToValue --> { value =>
            if value.isStreetAddress then clear(streetAddressErrorBus)
            else emit(streetAddressErrorBus, streetAddressInvalid)
          }
        },
        err(streetAddressErrorBus),
        cbar(
          btn("Save").amend {
            disabled <-- Model.userVar.signal.map { user => !user.isValid }
            onClick --> { _ =>
              val command = SaveUser(Model.userVar.now())
              log("profile view: save button onClick command: %o", command)
              call(command, handler)
            }
          } 
        )
      )
    )