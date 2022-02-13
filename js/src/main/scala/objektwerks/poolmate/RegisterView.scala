package objektwerks.poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validators.*

object RegisterView:
  def apply(emailAddress: Var[String]): HtmlElement =
    val emailAddressError = new EventBus[String]
    frm(
      hdr("Register"),
      lbl("Email"),
      email.amend {
        value <-- emailAddress
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> emailAddress
        onKeyUp.mapToValue --> { value =>
          if value.isEmailAddress then emailAddressError.emit("")
          else emailAddressError.emit("Enter a valid email address.")
        }
      },
      err(emailAddressError),
      cbar(
        btn("Register").amend {
          disabled <-- emailAddress.signal.map(email => !email.isEmailAddress)
        }
      ).amend {
        onSubmit --> { event =>
          event.preventDefault()
          log(s"email address: ${emailAddress.now()}")
          PageRouter.router.pushState(LoginPage)
        }
      } 
    )