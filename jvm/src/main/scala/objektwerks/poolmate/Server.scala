package objektwerks.poolmate

import cask.main.Main
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import io.undertow.Undertow

import scala.io.StdIn

object Server extends Main with LazyLogging:
  val conf = ConfigFactory.load("store.conf")
  
  val store = Store(conf)
  val emailSender = EmailSender(conf, store)
  val service = Service(store)
  val authorizer = Authorizer(service)
  val handler = Handler(emailSender, service)
  val validator = Validator()
  val dispatcher = Dispatcher(authorizer, validator, handler)

  val emailProcesor = EmailProcessor(conf, store)
  val scheduler = Scheduler(emailProcesor)

  val router = Router(dispatcher, store)
  val allRoutes = Seq(router)

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