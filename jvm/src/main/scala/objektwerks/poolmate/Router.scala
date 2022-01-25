package objektwerks.poolmate

import cask.main.Routes
import cask.model.Request
import com.typesafe.scalalogging.LazyLogging

import scala.io.{Codec, Source}
import scala.util.{Try, Using}

import Serializers.given

import upickle.default.{read, write}

object Router extends LazyLogging:
  private val utf8 = Codec.UTF8.name
  private val prefix = "/public/"
  private val html = loadResource("index.html")

  def loadResource(resource: String): String =
    val path = s"$prefix$resource"
    logger.debug(s"*** Load resource: $path")
    Using( Source.fromInputStream(getClass.getResourceAsStream(path), utf8) ) { 
      source => source.mkString
    }.getOrElse(s"*** Failed to load: $path")

class Router(dispatcher: Dispatcher) extends Routes with LazyLogging:
  @cask.get("/")
  def index() = Router.html

  @cask.get("/public")
  def public(request: cask.Request) = Router.loadResource(request.text())

  @cask.post("/command")
  def command(request: Request) =
    val command = read[Command](request.text())
    logger.debug(s"*** Command: $command")

    val event = dispatcher.dispatch(command)
    logger.debug(s"*** Event: $event")
    write[Event](event)

  initialize()