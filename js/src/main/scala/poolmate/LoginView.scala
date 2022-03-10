package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Error.*
import Validators.*

object LoginView:
  def apply(emailAddressVar: Var[String], pinVar: Var[String]): HtmlElement =
    val emailAddressErrorBus = new EventBus[String]
    val pinErrorBus = new EventBus[String]
    val errorBus = new EventBus[String]

    def handler(event: Either[Fault, Event]): Unit =
      event.fold(fault => errorBus.emit(s"Login failed: ${fault.cause}"), _ => PageRouter.router.pushState(PoolsPage))
      
    div(
      bar(
        btn("Home").amend {
          onClick --> { _ =>
            log("Login -> Home menu item onClick")
            PageRouter.router.pushState(IndexPage)
          }
        }      
      ),      
      hdr("Login"),
      lbl("Email Address"),
      email.amend {
        value <-- emailAddressVar
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> emailAddressVar
        onKeyUp.mapToValue --> { emailAddress =>
          if emailAddress.isEmailAddress then emailAddressErrorBus.emit("") else emailAddressErrorBus.emit(emailAddressError)
        }
      },
      err(emailAddressErrorBus),
      lbl("Pin"),
      pin.amend {
        value <-- pinVar
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> pinVar
        onKeyUp.mapToValue --> { pin =>
          if pin.isPin then pinErrorBus.emit("") else pinErrorBus.emit(pinError)
        }      
      },
      err(pinErrorBus),
      cbar(
        btn("Login").amend {
          disabled <-- emailAddressVar.signal.combineWithFn(pinVar.signal) {
            (email, pin) => !(email.isEmailAddress && pin.isPin)
          }
          onClick --> { _ =>
            log(s"Login onClick -> email address: ${emailAddressVar.now()} pin: ${pinVar.now()}")
            val command = Login(emailAddressVar.now(), pinVar.now())
            Proxy.call(command, handler)
          }
        }
      )
    )