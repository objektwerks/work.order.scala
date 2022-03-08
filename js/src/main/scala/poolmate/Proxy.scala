package poolmate

import org.scalajs.dom
import org.scalajs.dom.Headers
import org.scalajs.dom.HttpMethod
import org.scalajs.dom.RequestInit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Thenable.Implicits.*

import Serializers.given

import upickle.default.{read, write}

object Proxy:
  private val hdrs = new Headers {
      js.Array(
        js.Array("Content-Type", "application/json; charset=utf-8"),
        js.Array("Accept", "application/json")
      )
    }
  private val params = new RequestInit {
    method = HttpMethod.POST
    headers = hdrs
  }

  def now: Future[String] =
    ( 
      for
        response <- dom.fetch(Url.now)
        text     <- response.text()
      yield text
    ).recover { case error: Exception => s"Now failed: ${error.getMessage}" }

  def post(command: Command): Future[Event] =
    params.body = write[Command](command)
    ( 
      for
        response <- dom.fetch(Url.command, params)
        text     <- response.text()
      yield read[Event](text)
    ).recover { case error: Exception => Fault(s"$command failed: ${error.getMessage}") }