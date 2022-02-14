package objektwerks.poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Errors.*
import Validators.*

object LoginView:
  def apply(emailAddressVar: Var[String], pinVar: Var[String]): HtmlElement =
    val emailAddressErrors = new EventBus[String]
    val pinErrors = new EventBus[String]
    frm(
      hdr("Login"),
      lbl("Email"),
      email.amend {
        value <-- emailAddressVar
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> emailAddressVar
        onKeyUp.mapToValue --> { value =>
          if value.isEmailAddress then emailAddressErrors.emit("")
          else emailAddressErrors.emit(emailAddressError)
        }
      },
      err(emailAddressErrors),
      lbl("Pin"),
      pin.amend {
        value <-- pinVar
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> pinVar
        onKeyUp.mapToValue --> { value =>
          if value.isPin then pinErrors.emit("")
          else pinErrors.emit(pinError)
        }      
      },
      err(pinErrors),
      cbar(
        btn("Login").amend {
          disabled <-- emailAddressVar.signal.combineWithFn(pinVar.signal) {
            (email, pin) => !(email.isEmailAddress && pin.isPin)
          }
        }
      ).amend {
        onSubmit --> { event =>
          event.preventDefault()
          log(s"email address: ${emailAddressVar.now()} pin: ${pinVar.now()}")
          PageRouter.router.pushState(PoolsPage)
        }  
      }
    )