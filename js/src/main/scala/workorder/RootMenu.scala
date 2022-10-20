package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object RootMenu extends View:
  def apply(): HtmlElement =
    div(
      bar(
        btn("Login").amend {
          onClick --> { _ =>
            log("Root -> Login menu item onClick")
            route(LoginPage)
          }
        },
        rbtn("Register").amend {
          onClick --> { _ =>
            log(s"Root -> Register menu item onClick")
            route(RegisterPage)
          }
        }          
      )
    )