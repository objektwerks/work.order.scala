package objektwerks.poolmate

import cask.main.Routes
import cask.model.{Request, Response}
import com.typesafe.scalalogging.LazyLogging

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

import scala.io.{Codec, Source}
import scala.util.{Try, Using}

import Serializers.given

import upickle.default.{read, write}

trait Resources(val basePath: String) extends LazyLogging:
  val utf8 = Codec.UTF8.name
  val contentType = "Content-Type"
  val indexHtml = loadResource("index.html")
  val indexHtmlHeader = contentType -> "text/html; charset=UTF-8"

  def toContentType(resource: String): String = resource.split('.').last

  def toPath(resource: String): String = s"$basePath$resource"

  def toHeader(resource: String): (String, String) =
    logger.debug(s"*** to header: ${toContentType(resource)}")
    toContentType(resource) match
      case "css"  => contentType -> "text/css"
      case "ico"  => contentType -> "image/x-icon"
      case "png"  => contentType -> "image/png"
      case "js"   => contentType -> "text/javascript"
      case "map"  => contentType -> "application/json"
      case "html" => indexHtmlHeader
      case _      => contentType -> "text/plain"
  
  def isImage(resource: String): Boolean =
    toContentType(resource) match
      case "ico" | "png"  => true
      case _              => false

  def loadImage(resource: String): Array[Byte] =
    val path = toPath(resource)
    logger.debug(s"*** load image: $path")
    val url = getClass.getResource(path)
    val image = ImageIO.read(url)
    val baos = new ByteArrayOutputStream()
    val contentType = toContentType(resource)
    ImageIO.write(image, contentType, baos)
    baos.toByteArray

  def loadResource(resource: String): Array[Byte] =
    val path = toPath(resource)
    logger.debug(s"*** load resource: $path")
    Using( Source.fromInputStream(getClass.getResourceAsStream(path), utf8) ) {
      source => source.mkString.getBytes
    }.getOrElse(Array.empty[Byte])

class Router(dispatcher: Dispatcher) extends Routes with LazyLogging with Resources("/public/"):
  @cask.get("/")
  def index() = Response(indexHtml, 200, Seq(indexHtmlHeader))

  @cask.get(basePath, subpath = true)
  def resources(request: Request) =
    val resource = request.remainingPathSegments.head
    val headers = Seq(toHeader(resource))
    if isImage(resource) then Response(loadImage(resource), 200, headers)
    else Response(loadResource(resource), 200, headers)

  @cask.post("/command")
  def command(request: Request) =
    val command = read[Command](request.text())
    logger.debug(s"*** Command: $command")

    val event = dispatcher.dispatch(command)
    logger.debug(s"*** Event: $event")
    write[Event](event)

  initialize()