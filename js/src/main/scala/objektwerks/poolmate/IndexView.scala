package objektwerks.poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

object IndexView:
    def apply(): HtmlElement =
      div(
        div(cls("w3-bar"),
          button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo"), "Login").amend {
            onClick --> { _ =>
              log("Login onClick")
              PageRouter.router.pushState(LoginPage)
            }
          },
          button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo w3-right"), "Register").amend {
            onClick --> { _ =>
              log("Register onClick")
              PageRouter.router.pushState(RegisterPage)
            }
          }          
        )
      )