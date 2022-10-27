package workorder

import cask.main.Routes
import cask.model.{Request, Response}
import com.typesafe.scalalogging.LazyLogging

import java.time.Instant

import upickle.default.{read, write}

import Serializer.given

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

  @cask.post("/workorder/add")
  def addWorkOrder(request: Request) =
    write[WorkOrderAdded]( handler.addWorkOrder( read[AddWorkOrder](request.text()) ) )

  @cask.post("/workorder/save")
  def saveWorkOrder(request: Request) =
    write[WorkOrderSaved]( handler.saveWorkOrder( read[SaveWorkOrder](request.text()) ) )

  @cask.post("/workorders")
  def listWorkOrders(request: Request) =
    write[WorkOrdersListed]( handler.listWorkOrders( read[ListWorkOrders](request.text()) ) )

  initialize()