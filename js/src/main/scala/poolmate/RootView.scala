package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object RootView extends View:
  def apply(pinVar: Var[String], accountVar: Var[Account]): HtmlElement =
    def handler(event: Either[Fault, Event]): Unit =
      event match
        case Right(event) =>
          event match
            case Joined(account) =>
              clearErrors()
              accountVar.set(account)
              pinVar.set(account.pin)
              log(s"Root -> handler joined account: $account")
              route(EnterPage)
            case _ => log(s"Root view handler failed: $event")
        case Left(fault) => errorBus.emit(s"Join failed: ${fault.cause}")
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
            val command = Join()
            call(command, handler)
          }
        }          
      )
    )