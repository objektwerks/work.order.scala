package poolmate

import cask.main.Main
import cask.internal.DispatchTrie
import cask.util.Logger
import cask.main.Routes
import cask.router.EndpointMetadata
import cask.router.Decorator
import cask.model.Response
import cask.router.Result
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import io.undertow.Undertow
import io.undertow.server.HttpServerExchange
import io.undertow.server.handlers.BlockingHandler
import io.undertow.util.Headers
import io.undertow.util.HttpString

import java.util.concurrent.TimeUnit

import scala.concurrent.duration._
import scala.io.StdIn
import scala.jdk.CollectionConverters.*

given log: Logger = new Logger.Console()

object CorsHandler:
  val accessControlAllowOrigin = new HttpString("Access-Control-Allow-Origin")
  val accessControlAllowCredentials = new HttpString("Access-Control-Allow-Credentials")
  val acccessControlAllowHeaders = new HttpString("Access-Control-Allow-Headers")
  val accessControlAllowMethods = new HttpString("Access-Control-Allow-Methods")

class CorsHandler(dispatchTrie: DispatchTrie[Map[String, (Routes, EndpointMetadata[_])]],
                  mainDecorators: Seq[Decorator[_, _, _]],
                  debugMode: Boolean,
                  handleNotFound: () => Response.Raw,
                  handleMethodNotAllowed: () => Response.Raw,
                  handleError: (Routes, EndpointMetadata[_], Result.Error) => Response.Raw)
  extends Main.DefaultHandler(dispatchTrie,
                              mainDecorators,
                              debugMode,
                              handleNotFound,
                              handleMethodNotAllowed,
                              handleError)(using log: Logger):
  override def handleRequest(exchange: HttpServerExchange): Unit =
    exchange.getResponseHeaders
      .put(new HttpString("Access-Control-Allow-Origin"), "*")
      .put(new HttpString("Access-Control-Allow-Credentials"), "true")
      .putAll(new HttpString("Access-Control-Allow-Headers"), Set("Authorization", "Content-Type", "X-Requested-With").asJava)
      .putAll(new HttpString("Access-Control-Allow-Methods"), Set("POST", "GET", "PUT", "DELETE", "PATCH", "OPTIONS").asJava)
    super.handleRequest(exchange)