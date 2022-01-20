package objektwerks

import cask.main.Main
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import io.undertow.Undertow

import objektwerks.router.Router
import objektwerks.service.*

import scala.io.StdIn

object Server extends Main with LazyLogging:
  val store = SqlStore(ConfigFactory.load("store.conf"))
  val service = Service(store)
  val authorizer = Authorizer(service)
  val handler = Handler(service)
  val validator = Validator()
  val dispatcher = Dispatcher(authorizer, validator, handler)
  
  val allRoutes = Seq(Router(dispatcher))

  override def port: Int = 7272

  override def host: String = "localhost"

  override def main(args: Array[String]): Unit =
    if (!verbose) Main.silenceJboss()
    val server = Undertow.builder
      .addHttpListener(port, host)
      .setHandler(defaultHandler)
      .build

    server.start()
    val started = s"*** Server started at http://$host:$port/\nPress RETURN to stop..."
    println(started)
    logger.info(started)

    StdIn.readLine()
    server.stop()
    val stopped = s"*** Server stopped!"
    println(stopped)
    logger.info(stopped)