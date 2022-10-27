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
    val registered = handler.register( read[Register](request.text()) )
    write[Registered](registered)

  @cask.post("/login")
  def login(request: Request) =
    val loggedIn = handler.login( read[Login](request.text()) )
    write[LoggedIn](loggedIn)

  @cask.post("/user/save")
  def saveUser(request: Request) =
    val userSaved = handler.saveUser( read[SaveUser](request.text()) )
    write[UserSaved](userSaved)

  @cask.post("/workorder/add")
  def addWorkOrder(request: Request) =
    val workOrderAdded = handler.addWorkOrder( read[AddWorkOrder](request.text()) )
    write[WorkOrderAdded](workOrderAdded)

  @cask.post("/workorder/save")
  def saveWorkOrder(request: Request) =
    val workOrderSaved = handler.saveWorkOrder( read[SaveWorkOrder](request.text()) )
    write[WorkOrderSaved](workOrderSaved)

  @cask.post("/workorders")
  def listWorkOrders(request: Request) =
    val workOrdersListed = handler.listWorkOrders( read[ListWorkOrders](request.text()) )
    write[WorkOrdersListed](workOrdersListed)

  initialize()