package objektwerks.poolmate

import cask.main.Routes
import cask.model.{Request, Response}
import com.typesafe.scalalogging.LazyLogging

final class ResourceRouter(dispatcher: Dispatcher) extends Routes with LazyLogging with Resources("/public/"):
  @cask.get("/")
  def index() = Response(indexHtml, 200, Seq(indexHtmlHeader))

  @cask.get(basePath, subpath = true)
  def resources(request: Request) =
    val resource = request.remainingPathSegments.head
    val headers = Seq(toHeader(resource))
    if isImage(resource) then Response(loadImage(resource), 200, headers)
    else Response(loadResource(resource), 200, headers)

  initialize()