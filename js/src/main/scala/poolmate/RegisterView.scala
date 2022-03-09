package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import Components.*
import Error.*
import Validators.*

object RegisterView:
  def apply(emailAddressVar: Var[String]): HtmlElement =
    val emailAddressErrors = new EventBus[String]
    val errors = new EventBus[String]
    def handler(event: Either[Fault, Event]): Unit =
      event.fold(fault => errors.emit(s"Register failed: ${fault.cause}"), _ => PageRouter.router.pushState(IndexPage))
    div(
      hdr("Register"),
      note("Check your email for your new account details."),
      err(errors),
      lbl("Email Address"),
      email.amend {
        value <-- emailAddressVar
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> emailAddressVar
        onKeyUp.mapToValue --> { emailAddress =>
          if emailAddress.isEmailAddress then emailAddressErrors.emit("") else emailAddressErrors.emit(emailAddressError)
        }
      },
      err(emailAddressErrors),
      cbar(
        btn("Register").amend {
          disabled <-- emailAddressVar.signal.map(email => !email.isEmailAddress)
          onClick --> { _ =>
            log(s"Register onClick -> email address: ${emailAddressVar.now()}")
            val command = Register(emailAddressVar.now())
            Proxy.call(command, handler)
          }
        },
      )
    )