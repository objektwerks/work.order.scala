package objektwerks.poolmate

import cask.main.Routes
import cask.model.{Request, Response}
import com.typesafe.scalalogging.LazyLogging

import scala.io.{Codec, Source}
import scala.util.{Try, Using}

import Serializers.given

import upickle.default.{read, write}

object Router extends LazyLogging:
  private val utf8 = Codec.UTF8.name
  private val contentType = "Content-Type"
  private val basePath = "/public/"
  private val indexHtml = loadResource("index.html")
  private val indexHtmlHeader = contentType -> "text/html; charset=UTF-8"

  def loadResource(resource: String): String =
    val path = s"$basePath$resource"
    logger.debug(s"*** load resource: $path")
    Using( Source.fromInputStream(getClass.getResourceAsStream(path), utf8) ) {
      source => source.mkString
    }.getOrElse(s"failed to load resource: $path")

  def toHeader(resource: String): (String, String) =
    logger.debug(s"*** to header: ${resource.split('.').last}")
    resource.split('.').last match
      case "css"  => contentType -> "text/css"
      case "ico"  => contentType -> "image/x-icon"
      case "png"  => contentType -> "image/png"
      case "js"   => contentType -> "text/javascript"
      case "map"  => contentType -> "application/json"
      case "html" => indexHtmlHeader
      case _      => contentType -> "text/plain"

class Router(dispatcher: Dispatcher) extends Routes with LazyLogging:
  import Router._

  @cask.get("/")
  def index() = Response(indexHtml, 200, Seq(indexHtmlHeader))

  @cask.get(basePath, subpath = true)
  def resources(request: Request) =
    val resource = request.remainingPathSegments.head
    val content = loadResource(resource)
    val headers = Seq(toHeader(resource))
    logger.debug(s"*** headers: $headers")
    Response(content, 200, headers)

  @cask.post("/command")
  def command(request: Request) =
    val command = read[Command](request.text())
    logger.debug(s"*** Command: $command")

    val event = dispatcher.dispatch(command)
    logger.debug(s"*** Event: $event")
    write[Event](event)

  initialize()