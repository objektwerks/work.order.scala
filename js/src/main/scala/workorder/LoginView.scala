package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object LoginView extends View:
  def apply(): HtmlElement =
    val emailAddressVar = Var("")
    val pinVar = Var("")
    val emailAddressErrorBus = new EventBus[String]
    val pinErrorBus = new EventBus[String]

    def handler(either: Either[Fault, Event]): Unit =
      either match
        case Left(fault) => errorBus.emit(s"Login failed: ${fault.cause}")
        case Right(event) =>
          event match
            case LoggedIn(_, _, _, _, _) =>
              clearErrors()
              // TODO set model
              route(WorkOrdersPage)
            case _ => log("login view: handler failed: %o", event)
      
    div(      
      hdr("Login"),
      lbl("Email Address"),
      email.amend {
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> emailAddressVar
        onKeyUp.mapToValue --> { emailAddress =>
          if emailAddress.isEmailAddress then clear(emailAddressErrorBus) 
          else emit(emailAddressErrorBus, emailAddressInvalid)
        }
      },
      err(emailAddressErrorBus),
      lbl("Pin"),
      pin.amend {
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> pinVar
        onKeyUp.mapToValue --> { pin =>
          if pin.isPin then clear(pinErrorBus)
          else emit(pinErrorBus, pinInvalid)
        }      
      },
      err(pinErrorBus),
      cbar(
        btn("Login").amend {
          disabled <-- pinVar.signal.map( pin => !pin.isPin )
          onClick --> { _ =>
            val command = Login(emailAddressVar.now(), pinVar.now())
            log("login view: button onClick command: %o", command)
            call(command, handler)
          }
        }
      )
    )