package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import Components.*
import Error.*
import Message.*
import Validators.*

object RegisterView extends View:
  def apply(pinVar: Var[String], accountVar: Var[Account]): HtmlElement =
    def handler(event: Either[Fault, Event]): Unit =
      event match
        case Right(event) =>
          event match
            case Registered(account) =>
              clearErrors()
              accountVar.set(account)
              pinVar.set(account.pin)
              route(LoginPage)
            case _ => log(s"Register view handler failed: $event")
        case Left(fault) => errorBus.emit(s"Register failed: ${fault.cause}")
      
    div(
      hdr("Register"),
      info(registerMessage),
      cbar(
        btn("Register").amend {
          onClick --> { _ =>
            log(s"Register onClick")
            val command = Register()
            call(command, handler)
          }
        },
      )
    )