package workorder

import com.raquo.laminar.api.L._

import org.scalajs.dom
import org.scalajs.dom.Headers
import org.scalajs.dom.HttpMethod
import org.scalajs.dom.RequestInit
import org.scalajs.dom.console.log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Thenable.Implicits.*

import Serializer.given

import upickle.default.{read, write}

object Fetcher:
  val post = "post"
  val jsonHeaders = new Headers {
    js.Array(
      js.Array("Content-Type", "application/json; charset=UTF-8"),
      js.Array("Accept", "application/json")
    )
  }
  val formDataHeaders = new Headers {
    js.Array()
  }
  val jsonParameters = new RequestInit {
    method = HttpMethod.POST
    headers = jsonHeaders
  }
  val formDataParameters = new RequestInit {
    method = HttpMethod.POST
    headers = formDataHeaders
  }

  def now: Future[String] =
    (
      for
        response <- dom.fetch(Urls.now)
        text <- response.text()
      yield text
      ).recover {
      case failure: Exception => s"Now failed: ${failure.getMessage}"
    }

  def call(command: Command, handler: (either: Either[Fault, Event]) => Unit) =
    val event: Future[Event] = command match
      case Register(_, _, _, _) => post(command, jsonParameters, Urls.register)
      case Login(_, _) => post(command, jsonParameters, Urls.login)
      case SaveUser(_) => post(command, jsonParameters, Urls.userSave)
      case AddWorkOrder(_, _) => post(command, jsonParameters, Urls.workOrderAdd)  // TODO for form data support!
      case SaveWorkOrder(_, _) => post(command, jsonParameters, Urls.workOrderSave)  // TODO for form data support!
      case ListWorkOrders(_, _) => post(command, jsonParameters, Urls.workOrdersList)

    handle(event, handler)

  private def post(command: Command, parameters: RequestInit, url: String): Future[Event] =
    log(s"Proxy:post command: $command")
    parameters.body = write[Command](command)
    log(s"Proxy:post params: $parameters")
    log(s"Proxy:post url: $url")
    (
      for
        response <- dom.fetch(url, parameters)
        text <- response.text()
      yield
        log(s"Proxy:post text: $text")
        val event = read[Event](text)
        log (s"Proxy:post event: $event")
        event
      ).recover {
      case failure: Exception =>
        log(s"Proxy:post failure: ${failure.getCause}")
        Fault(failure)
    }

  private def handle(future: Future[Event], handler: (either: Either[Fault, Event]) => Unit): Unit =
    future map { event =>
      handler(
        event match
          case fault: Fault =>
            log(s"Proxy:handle fault: $fault")
            Left(fault)
          case event: Event =>
            log(s"Proxy:handle event: $event")
            Right(event)
      )
    }