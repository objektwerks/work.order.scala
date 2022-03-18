package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import Components.*
import Error.*
import Message.*
import Validators.*

object ExploreView extends View:
  def apply(pinVar: Var[String], accountVar: Var[Account]): HtmlElement =
    def handler(event: Either[Fault, Event]): Unit =
      event match
        case Right(event) =>
          event match
            case Explored(account) =>
              clearErrors()
              accountVar.set(account)
              pinVar.set(account.pin)
              route(LoginPage)
            case _ => log(s"Explore view handler failed: $event")
        case Left(fault) => errorBus.emit(s"Explore failed: ${fault.cause}")
    div(
      hdr("Explore"),
      info(exploreMessage),
      cbar(
        btn("Explore").amend {
          onClick --> { _ =>
            log(s"Explore onClick")
            val command = Explore()
            call(command, handler)
          }
        },
      )
    )