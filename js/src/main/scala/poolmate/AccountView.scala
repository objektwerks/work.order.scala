package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object AccountView:
  def apply(account: Var[Account]): HtmlElement =
    val errorBus = new EventBus[String]

    def deactivateHandler(event: Either[Fault, Event]): Unit =
      event.fold(fault => errorBus.emit(s"Deactivate failed: ${fault.cause}"), _ => PageRouter.router.pushState(PoolsPage))

    def reactivateHandler(event: Either[Fault, Event]): Unit =
      event.fold(fault => errorBus.emit(s"Reactivate failed: ${fault.cause}"), _ => PageRouter.router.pushState(PoolsPage))

    div(
      bar(
        btn("Pools").amend {
          onClick --> { _ =>
            log("Account -> Pools onClick")
            PageRouter.router.pushState(PoolsPage)
          }
        }      
      ),
      div(
        hdr("Account"),
        lbl("License"),
        rotxt.amend {
          value <-- account.signal.map(_.license)
        },
        lbl("Email Address"),
        rotxt.amend {
          value <-- account.signal.map(_.emailAddress)
        },
        lbl("Pin"),
        rotxt.amend {
          value <-- account.signal.map(_.pin)
        },
        lbl("Activated"),
        rotxt.amend {
          value <-- account.signal.map(_.activated.toString)
        },
        lbl("Deactivated"),
        rotxt.amend {
          value <-- account.signal.map(_.deactivated.toString)
        },
        cbar(
          btn("Deactivate").amend {
            onClick --> { _ =>
              log("Account -> Deactivate onClick")
              val command = Deactivate(account.now().license)
              PageRouter.router.pushState(PoolsPage)
            }
          },
          btn("Reactivate").amend {
            onClick --> { _ =>
              log("Account -> Reactivate onClick")
              val command = Reactivate(account.now().license)
              PageRouter.router.pushState(PoolsPage)
            }
          }      
        )
      )
    )