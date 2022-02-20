package poolmate

import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import scala.scalajs.js.Thenable.Implicits._

object Proxy:
  def fetch(url: String): Future[String] = {
    ( for {
      response <- dom.fetch(url)
      text     <- response.text()
    } yield {
      text
    } ).recover { case error: Exception => error.getMessage }
  }