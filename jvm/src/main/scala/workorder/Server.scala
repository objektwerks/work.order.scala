package workorder

import cask.main.Main
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import io.undertow.Undertow
import io.undertow.server.handlers.BlockingHandler

import scala.concurrent.duration.*
import scala.io.StdIn

object Server extends Main with LazyLogging:
  private val conf = ConfigFactory.load("server.conf")

  private val emailer = Emailer(conf)
  Store.dirs(conf)
  private val store = Store(conf, Store.cache(minSize = 4, maxSize = 10, expireAfter = 24.hour))
  private val service = Service(emailer, store)
  private val handler = Handler(service)
  private val router = Router(handler)

  override val allRoutes = Seq(router)
  
  override def host: String = conf.getString("host")

  override def port: Int = conf.getInt("port")

  override def defaultHandler: BlockingHandler =
    new BlockingHandler( CorsHandler(dispatchTrie,
                                     mainDecorators,
                                     debugMode = false,
                                     handleNotFound,
                                     handleMethodNotAllowed,
                                     handleEndpointError) )

  override def main(args: Array[String]): Unit =
    Main.silenceJboss()    
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