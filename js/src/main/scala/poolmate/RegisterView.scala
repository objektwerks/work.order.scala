package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import Components.*
import Error.*
import Message.*
import Validator.*

object RegisterView extends View:
  def apply(): HtmlElement =
    val emailAddressErrorBus = new EventBus[String]

    def handler(either: Either[Throwable, Event]): Unit =
      either match
        case Left(fault) => errorBus.emit(s"Register failed: ${fault.cause}")
        case Right(event) =>
          event match
            case Registered(_, success, error) =>
              clearErrors()
              log(s"Registered -> handler registered.")
              route(LoginPage)
            case _ => log(s"Join -> handler failed: $event")
      
    div(
      hdr("Register"),
      info(joinMessage),
      err(errorBus),
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
      cbar(
        btn("Join").amend {
          disabled <-- emailAddressVar.signal.map(email => !email.isEmailAddress)
          onClick --> { _ =>
            log(s"Join button onClick -> email address: ${emailAddressVar.now()}")
            val command = Join(emailAddressVar.now())
            call(command, handler)
          }
        },
      )
    )