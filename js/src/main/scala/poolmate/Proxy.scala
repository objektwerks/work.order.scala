package poolmate

import org.scalajs.dom
import org.scalajs.dom.Headers
import org.scalajs.dom.HttpMethod
import org.scalajs.dom.RequestInit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import scala.scalajs.js
import scala.scalajs.js.Thenable.Implicits._

import Serializers.given

import upickle.default.{read, write}

object Proxy:
  private val hdrs = new Headers {
      js.Array(
        js.Array("Content-Type", "application/json; charset=utf-8"),
        js.Array("Accept", "application/json")
      )
    }

  def get(url: String): Future[String] =
    ( 
      for {
        response <- dom.fetch(url)
        text     <- response.text()
      } yield {
        text
      } 
    ).recover {
      case error: Exception => error.getMessage
    }

  def post(command: Command): Future[Event] =
    val init = new RequestInit {
      method = HttpMethod.POST
      body = write[Command](command)
      headers = hdrs
    }
    ( 
      for {
        response <- dom.fetch(Url.command, init)
        text     <- response.text()
      } yield {
        read[Event](text)
      } 
    ).recover {
      case error: Exception => Fault(error.getMessage)
    }