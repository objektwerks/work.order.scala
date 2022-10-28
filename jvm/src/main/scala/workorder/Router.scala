package workorder

import cask.main.Routes
import cask.model.{Request, Response}
import com.typesafe.scalalogging.LazyLogging

import java.time.Instant

import upickle.default.{read, write}

import Serializer.given

/* Note: This router can be readily set up as:
   https://github.com/objektwerks/poolmate.x/blob/main/jvm/src/main/scala/poolmate/Router.scala
   But this router is a replica of: https://github.com/objektwerks/work.order for various reasons,
   to include the optional support of images. Image file support is implemented in js/Fetcher. But
   it is not implemented in this router. Scala has no JS multer library. :) TODO!
*/
final class Router(handler: Handler) extends Routes with LazyLogging:
  @cask.get("/now")
  def now() = Response(Instant.now.toString)

  @cask.post("/register")
  def register(request: Request) =
    write[Registered]( handler.register( read[Register](request.text()) ) )

  @cask.post("/login")
  def login(request: Request) =
    write[LoggedIn]( handler.login( read[Login](request.text()) ) )

  @cask.post("/user/save")
  def saveUser(request: Request) =
    write[UserSaved]( handler.saveUser( read[SaveUser](request.text()) ) )

  @cask.post("/workorder/add") // only setup for a possible image file
  def addWorkOrder(request: Request) =
    write[WorkOrderAdded]( handler.addWorkOrder( read[AddWorkOrder](request.text()) ) )

  @cask.post("/workorder/save") // only setup for a possible image file
  def saveWorkOrder(request: Request) =
    write[WorkOrderSaved]( handler.saveWorkOrder( read[SaveWorkOrder](request.text()) ) )

  @cask.post("/workorders")
  def listWorkOrders(request: Request) =
    write[WorkOrdersListed]( handler.listWorkOrders( read[ListWorkOrders](request.text()) ) )

  initialize()