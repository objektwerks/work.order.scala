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
    val emailAddressErrorBus = new EventBus[String]

    def handler(either: Either[Fault, Event]): Unit =
      either match
        case Left(fault) => errorBus.emit(s"Register failed: ${fault.cause}")
        case Right(event) =>
          event match
            case Registered(_, _, _) =>
              clearErrors()
              log(s"Registered -> handler registered.")
              route(LoginPage)
            case _ => log(s"Register -> handler failed: $event")
      
    div(
      hdr("Register"),
      info(registerMessage),
      err(errorBus),
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
      cbar(
        btn("Register").amend {
          onClick --> { _ =>
            log(s"Register button onClick -> email address: ${emailAddressVar.now()}")
            val command = Register(roleVar.now(), nameVar.now(), emailAddressVar.now(), streetAddressVar.now())
            call(command, handler)
          }
        },
      )
    )