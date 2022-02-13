package objektwerks.poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Errors.*
import Validators.*

object RegisterView:
  def apply(emailAddressVar: Var[String]): HtmlElement =
    val emailAddressError = new EventBus[String]
    frm(
      hdr("Register"),
      lbl("Email"),
      email.amend {
        value <-- emailAddressVar
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> emailAddressVar
        onKeyUp.mapToValue --> { value =>
          if value.isEmailAddress then emailAddressError.emit("")
          else emailAddressError.emit(emailError)
        }
      },
      err(emailAddressError),
      cbar(
        btn("Register").amend {
          disabled <-- emailAddressVar.signal.map(email => !email.isEmailAddress)
        }
      ).amend {
        onSubmit --> { event =>
          event.preventDefault()
          log(s"email address: ${emailAddressVar.now()}")
          PageRouter.router.pushState(LoginPage)
        }
      } 
    )