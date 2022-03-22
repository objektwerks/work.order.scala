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
                route(EnterPage)
              case _ => log(s"Index -> Join view handler failed: $event")
          case Left(fault) => errorBus.emit(s"Join failed: ${fault.cause}")
      div(
        bar(
          btn("Enter").amend {
            onClick --> { _ =>
              log("Index -> Enter menu item onClick")
              route(EnterPage)
            }
          },
          rbtn("Join").amend {
            onClick --> { _ =>
              log(s"Index -> Join menu item onClick")
              val command = Join()
              call(command, handler)
            }
          }          
        )
      )