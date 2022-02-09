package objektwerks.poolmate

import cask.main.Routes
import cask.model.{Request, Response}
import com.typesafe.scalalogging.LazyLogging

import java.util.concurrent.TimeUnit

import scala.concurrent.duration._

final class ResourceRouter(dispatcher: Dispatcher)
  extends Routes
  with LazyLogging
  with Resources(
    basePath = "/public/",
    indexHtml = "index.html",
    cache = ResourcesCache(minSize = 4, maxSize = 10, expireAfter = 24.hour)):
  @cask.get("/")
  def index() = Response(loadResource(indexHtml), 200, Seq(htmlHeader))

  @cask.get(basePath, subpath = true)
  def resources(request: Request) =
    val resource = request.remainingPathSegments.head
    val headers = Seq(toHeader(resource))
    if isImage(resource) then Response(loadImage(resource), 200, headers)
    else Response(loadResource(resource), 200, headers)

  initialize()