package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Error.*
import Validators.*

object LoginView:
  def apply(emailAddressVar: Var[String], pinVar: Var[String]): HtmlElement =
    val emailAddressErrors = new EventBus[String]
    val pinErrors = new EventBus[String]
    val errorBus = new EventBus[String]
    def handler(event: Either[Fault, Event]): Unit =
      event.fold(fault => errorBus.emit(s"Login failed: ${fault.cause}"), _ => PageRouter.router.pushState(PoolsPage))
    div(
      hdr("Login"),
      lbl("Email Address"),
      email.amend {
        value <-- emailAddressVar
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> emailAddressVar
        onKeyUp.mapToValue --> { emailAddress =>
          if emailAddress.isEmailAddress then emailAddressErrors.emit("") else emailAddressErrors.emit(emailAddressError)
        }
      },
      err(emailAddressErrors),
      lbl("Pin"),
      pin.amend {
        value <-- pinVar
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> pinVar
        onKeyUp.mapToValue --> { pin =>
          if pin.isPin then pinErrors.emit("") else pinErrors.emit(pinError)
        }      
      },
      err(pinErrors),
      cbar(
        btn("Login").amend {
          disabled <-- emailAddressVar.signal.combineWithFn(pinVar.signal) {
            (email, pin) => !(email.isEmailAddress && pin.isPin)
          }
          onClick --> { _ =>
            log(s"Login onClick -> email address: ${emailAddressVar.now()} pin: ${pinVar.now()}")
            PageRouter.router.pushState(PoolsPage)
          }
        }
      )
    )