package poolmate

import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import scala.scalajs.js.Thenable.Implicits._

import Serializers.given

import upickle.default.{read, write}

object Proxy:
  private val headers = Map(
    "Content-Type" -> "application/json; charset=utf-8",
    "Accept" -> "application/json"
  )

  def get(url: String): Future[String] =
    ( for {
      response <- dom.fetch(url)
      text     <- response.text()
    } yield {
      text
    } ).recover {
      case error: Exception => error.getMessage
    }

  def post(url: String, command: Command): Future[Event] =
    ( for {
      response <- dom.fetch(url)
      text     <- response.text()
    } yield {
      read[Event](text)
    } ).recover {
      case error: Exception => Fault(error.getMessage)
    }