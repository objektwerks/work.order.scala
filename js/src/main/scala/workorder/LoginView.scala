package workorder

import com.raquo.laminar.api.L.*
import org.scalajs.dom.console.log
import Components.*
import Validator.*

object LoginView extends View:
  def apply(): HtmlElement =
    val emailAddressVar = Var("")
    val pinVar = Var("")

    def handler(either: Either[Fault, Event]): Unit =
      either match
        case Left(fault) => errorBus.emit(s"Login failed: ${fault.cause}")
        case Right(event) =>
          event match
            case LoggedIn(user, serviceProviders, workOrders, _, _) =>
              clearErrorBus()
              Model.userVar.set(user)
              Model.serviceProvidersVar.set(serviceProviders)
              Model.workOrdersVar.set(workOrders)
              route(WorkOrdersPage)
            case _ => errorBus.emit(s"Login failed: $event")
      
    div(      
      hdr("Login"),
      err(errorBus),
      lbl("Email Address"),
      email.amend {
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> emailAddressVar
      },
      lbl("Pin"),
      pin.amend {
        onInput.mapToValue.filter(_.nonEmpty).setAsValue --> pinVar
      },
      cbar(
        btn("Login").amend {
          onClick --> { _ =>
            val command = Login(emailAddressVar.now(), pinVar.now())
            val errors = command.validate
            log("login view: login button onClick command: %o", command)
            call(command, handler)
          }
        }
      )
    )