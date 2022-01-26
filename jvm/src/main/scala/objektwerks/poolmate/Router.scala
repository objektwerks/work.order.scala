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
  private val prefix = "/public/"
  private val indexHtml = loadResource("index.html")
  private val indexHtmlHeader = contentType -> "text/html; charset=UTF-8"

  def loadResource(resource: String): Array[Byte] =
    val path = s"$prefix$resource"
    logger.debug(s"*** load resource: $path")
    Using( Source.fromInputStream(getClass.getResourceAsStream(path), utf8) ) { 
      source => source.mkString.getBytes
    }.getOrElse(Array.empty[Byte])

  def toHeader(resource: String): (String, String) =
    logger.debug(s"*** to header: ${resource.split('.')(1)}")
    resource.split('.')(1) match
      case "ico"  => contentType -> "image/x-icon"
      case "png"  => contentType -> "image/png"
      case "js"   => contentType -> "text/javascript"
      case "html" => indexHtmlHeader
      case _      => contentType -> "text/plain"

class Router(dispatcher: Dispatcher) extends Routes with LazyLogging:
  import Router._

  @cask.get("/")
  def index() = Response(indexHtml, 200, Seq(indexHtmlHeader))

  @cask.get(Router.prefix, subpath = true)
  def resources(request: Request) =
    val resource = request.remainingPathSegments.head
    val content = loadResource(resource)
    val headers = Seq(toHeader(resource))
    Response(content, 200, headers)

  @cask.post("/command")
  def command(request: Request) =
    val command = read[Command](request.text())
    logger.debug(s"*** Command: $command")

    val event = dispatcher.dispatch(command)
    logger.debug(s"*** Event: $event")
    write[Event](event)

  initialize()