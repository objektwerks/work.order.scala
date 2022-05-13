package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Error.*
import Message.*
import Validators.*

object EnterView extends View:
  def apply(emailAddressVar: Var[String], pinVar: Var[String], accountVar: Var[Account]): HtmlElement =
    val emailAddressErrorBus = new EventBus[String]
    val pinErrorBus = new EventBus[String]

    def handler(either: Either[Fault, Event]): Unit =
      either match
        case Left(fault) => errorBus.emit(s"Enter failed: ${fault.cause}")
        case Right(event) =>
          event match
            case Entered(account) =>
              clearErrors()
              accountVar.set(account)
              route(AppPage)
            case _ => log(s"Enter -> handler failed: $event")
      
    div(      
      hdr("Enter"),
      lbl("Email Address"),
      email.amend {
        value <-- emailAddressVar
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
        value <-- pinVar
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> pinVar
        onKeyUp.mapToValue --> { pin =>
          if pin.isPin then clear(pinErrorBus) else emit(pinErrorBus, pinError)
        }      
      },
      err(pinErrorBus),
      cbar(
        btn("Enter").amend {
          disabled <-- pinVar.signal.map( pin => !pin.isPin )
          onClick --> { _ =>
            log(s"Enter button onClick -> pin: ${pinVar.now()}")
            val command = Enter(emailAddressVar.now(), pinVar.now())
            call(command, handler)
          }
        }
      )
    )