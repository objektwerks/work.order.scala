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

import java.util.concurrent.TimeUnit

import scala.concurrent.duration._
import scala.io.StdIn
import cask.model.Response.Data

given log: Logger = new Logger.Console()

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
    super.handleRequest(exchange)