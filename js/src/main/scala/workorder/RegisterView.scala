package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import Components.*
import Validator.*

object RegisterView extends View:
  def apply(): HtmlElement =
    val roleVar = Var("")
    val nameVar = Var("")
    val emailAddressVar = Var("")
    val streetAddressVar = Var("")
    val nameErrorBus = new EventBus[String]
    val emailAddressErrorBus = new EventBus[String]
    val streetAddressErrorBus = new EventBus[String]

    def handler(either: Either[Fault, Event]): Unit =
      either match
        case Left(fault) => errorBus.emit(s"Register failed: ${fault.cause}")
        case Right(event) =>
          event match
            case Registered(_, _, _) =>
              clearErrorBus()
              log("register view: handler registered.")
              route(LoginPage)
            case _ => log("register view: handler failed: %o", event)
      
    div(
      hdr("Register"),
      err(errorBus),
      lbl("Role"),
      email.amend {
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> roleVar
      },
      lbl("Name"),
      email.amend {
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> nameVar
        onKeyUp.mapToValue --> { value =>
          if value.isName then clear(nameErrorBus)
          else emit(nameErrorBus, nameInvalid)
        }
      },
      err(nameErrorBus),
      lbl("Email Address"),
      email.amend {
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> emailAddressVar
        onKeyUp.mapToValue --> { value =>
          if value.isEmailAddress then clear(emailAddressErrorBus)
          else emit(emailAddressErrorBus, emailAddressInvalid)
        }
      },
      err(emailAddressErrorBus),
      lbl("Street Address"),
      email.amend {
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> streetAddressVar
        onKeyUp.mapToValue --> { value =>
          if value.isStreetAddress then clear(streetAddressErrorBus)
          else emit(streetAddressErrorBus, streetAddressInvalid)
        }
      },
      err(emailAddressErrorBus),
      cbar(
        btn("Register").amend {
          disabled <-- nameVar.signal.combineWithFn(emailAddressVar.signal, streetAddressVar.signal) {
            (name, email, street) => !(name.isName && email.isEmailAddress && street.isStreetAddress)
          }
          onClick --> { _ =>
            val command = Register(roleVar.now(), nameVar.now(), emailAddressVar.now(), streetAddressVar.now())
            log("register view: button onClick command: %o", command)
            call(command, handler)
          }
        },
      )
    )