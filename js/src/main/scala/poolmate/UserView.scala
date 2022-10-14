package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Validator.*

object UserView extends View:
  def apply(userVar: Var[User]): HtmlElement =
    div(
      bar(
        btn("App").amend {
          onClick --> { _ =>
            log("Account -> App menu item onClick")
            route(AppPage)
          }
        }      
      ),
      div(
        hdr("User"),
        lbl("License"),
        rotxt.amend {
          value <-- userVar.signal.map(_.license)
        },
        lbl("Pin"),
        rotxt.amend {
          value <-- userVar.signal.map(_.pin)
        },
        cbar(
          btn("Save").amend {
            onClick --> { _ =>
              log("User -> Save button onClick")
              val command = SaveUser(userVar.now())
              call(command, deactivateHandler)
            }
          } 
        )
      )
    )