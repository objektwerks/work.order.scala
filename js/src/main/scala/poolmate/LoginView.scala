package poolmate

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
        case Left(fault) => errorBus.emit(s"Enter failed: ${fault.cause}")
        case Right(event) =>
          event match
            case LoggedIn(_, _, _, _, _) =>
              clearErrors()
              // TODO set model
              route(AppPage)
            case _ => log(s"Login -> handler failed: $event")
      
    div(      
      hdr("Login"),
      lbl("Email Address"),
      email.amend {
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> emailAddressVar
        onKeyUp.mapToValue --> { emailAddress =>
          if emailAddress.isEmailAddress 
          then clear(emailAddressErrorBus) 
          else emit(emailAddressErrorBus, emailAddressError)
        }
      },
      err(emailAddressErrorBus),
      info(pinMessage),
      lbl("Pin"),
      pin.amend {
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> pinVar
        onKeyUp.mapToValue --> { pin =>
          if pin.isPin
          then clear(pinErrorBus)
          else emit(pinErrorBus, pinError)
        }      
      },
      err(pinErrorBus),
      cbar(
        btn("Login").amend {
          disabled <-- pinVar.signal.map( pin => !pin.isPin )
          onClick --> { _ =>
            log(s"Login button onClick -> pin: ${pinVar.now()}")
            val command = Login(emailAddressVar.now(), pinVar.now())
            call(command, handler)
          }
        }
      )
    )