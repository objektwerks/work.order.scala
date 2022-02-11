package objektwerks.poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object IndexView:
    def apply(): HtmlElement =
      div(
        div(cls("w3-bar"),
          btn("Login").amend {
            onClick --> { _ =>
              log("Login onClick")
              PageRouter.router.pushState(LoginPage)
            }
          },
          rbtn("Register").amend {
            onClick --> { _ =>
              log("Register onClick")
              PageRouter.router.pushState(RegisterPage)
            }
          }          
        )
      )