package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object AccountView:
  def apply(accountVar: Var[Account]): HtmlElement =
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
          value <-- accountVar.signal.map(_.license)
        },
        lbl("Email Address"),
        rotxt.amend {
          value <-- accountVar.signal.map(_.emailAddress)
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
            disabled <-- accountVar.signal.map { account => account.deactivated > 0 }
            onClick --> { _ =>
              log("Account -> Deactivate onClick")
              val command = Deactivate(accountVar.now().license)
              Proxy.call(command, deactivateHandler)
            }
          },
          btn("Reactivate").amend {
            disabled <-- accountVar.signal.map { account => account.activated > 0 }
            onClick --> { _ =>
              log("Account -> Reactivate onClick")
              val command = Reactivate(accountVar.now().license)
              Proxy.call(command, reactivateHandler)
            }
          }      
        )
      )
    )