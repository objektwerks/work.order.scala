package objektwerks.poolmate

import com.github.blemale.scaffeine.{ Cache, Scaffeine }
import com.typesafe.scalalogging.LazyLogging

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

import scala.concurrent.duration._
import scala.io.{Codec, Source}
import scala.util.{Try, Using}

trait Resources(val basePath: String) extends LazyLogging:
  private val utf8 = Codec.UTF8.name
  private val contentType = "Content-Type"
  private val cssHeader = contentType -> "text/css"
  private val icoHeader = contentType -> "image/x-icon"
  private val pngHeader = contentType -> "image/png"
  private val jsHeader = contentType -> "text/javascript"
  private val jsmapHeader = contentType -> "application/json"
  private val textHeader = contentType -> "text/plain"
  val htmlHeader = contentType -> "text/html; charset=UTF-8"

  private val cache: Cache[String, Array[Byte]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(24.hour)
      .maximumSize(100)
      .build[String, Array[Byte]]()

  val indexHtml = loadResource("index.html")    

  def toContentType(resource: String): String = resource.split('.').last

  def toPath(resource: String): String = s"$basePath$resource"

  def toHeader(resource: String): (String, String) =
    logger.debug(s"*** to header: ${toContentType(resource)}")
    toContentType(resource) match
      case "css"  => cssHeader
      case "ico"  => icoHeader
      case "png"  => pngHeader
      case "js"   => jsHeader
      case "map"  => jsmapHeader
      case "html" => htmlHeader
      case _      => textHeader
  
  def isImage(resource: String): Boolean =
    toContentType(resource) match
      case "ico" | "png"  => true
      case _              => false

  def loadImage(resource: String): Array[Byte] =
    cache.getIfPresent(resource) match
      case Some(bytes) => bytes
      case None =>
        val path = toPath(resource)
        logger.debug(s"*** load image: $path")
        val url = getClass.getResource(path)
        val image = ImageIO.read(url)
        val baos = new ByteArrayOutputStream()
        val contentType = toContentType(resource)
        ImageIO.write(image, contentType, baos)
        val bytes = baos.toByteArray
        cache.put(resource, bytes)
        logger.debug(s"*** cache put: $resource")
        bytes

  def loadResource(resource: String): Array[Byte] =
    cache.getIfPresent(resource) match
      case Some(bytes) => bytes
      case None =>
        val path = toPath(resource)
        logger.debug(s"*** load resource: $path")
        val bytes = Using( Source.fromInputStream(getClass.getResourceAsStream(path), utf8) ) {
          source => source.mkString.getBytes
        }.getOrElse(Array.empty[Byte])
        cache.put(resource, bytes)
        bytes