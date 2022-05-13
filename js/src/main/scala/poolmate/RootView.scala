package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object RootView extends View:
  def apply(emailAddressVar: Var[String], pinVar: Var[String], accountVar: Var[Account]): HtmlElement =
    div(
      bar(
        btn("Enter").amend {
          onClick --> { _ =>
            log("Root -> Enter menu item onClick")
            route(EnterPage)
          }
        },
        rbtn("Join").amend {
          onClick --> { _ =>
            log(s"Root -> Join menu item onClick")
            route(JoinPage)
          }
        }          
      )
    )