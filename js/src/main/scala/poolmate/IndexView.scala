package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object IndexView extends View:
    def apply(): HtmlElement =
      div(
        bar(
          btn("Login").amend {
            onClick --> { _ =>
              log("Index -> Login menu item onClick")
              PageRouter.router.pushState(LoginPage)
            }
          },
          rbtn("Register").amend {
            onClick --> { _ =>
              log("Index -> Register menu item onClick")
              PageRouter.router.pushState(RegisterPage)
            }
          }          
        )
      )