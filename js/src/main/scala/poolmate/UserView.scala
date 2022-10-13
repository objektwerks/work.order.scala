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
        hdr("Account"),
        lbl("License"),
        rotxt.amend {
          value <-- accountVar.signal.map(_.license)
        },
        lbl("Pin"),
        rotxt.amend {
          value <-- accountVar.signal.map(_.pin)
        },
        lbl("Activated"),
        rotxt.amend {
          value <-- accountVar.signal.map(_.activated.toString)
        },
        lbl("Deactivated"),
        rotxt.amend {
          value <-- accountVar.signal.map(_.deactivated.toString)
        },
        cbar(
          btn("Deactivate").amend {
            disabled <-- accountVar.signal.map { account => if account.isDeactivated then true else false }
            onClick --> { _ =>
              log("Account -> Deactivate button onClick")
              val command = Deactivate(accountVar.now().license)
              call(command, deactivateHandler)
            }
          },
          btn("Reactivate").amend {
            disabled <-- accountVar.signal.map { account => if account.isActivated then true else false }
            onClick --> { _ =>
              log("Account -> Reactivate button onClick")
              val command = Reactivate(accountVar.now().license)
              call(command, reactivateHandler)
            }
          }      
        )
      )
    )