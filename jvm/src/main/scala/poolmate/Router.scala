package poolmate

import cask.main.Routes
import cask.model.{Request, Response}
import com.typesafe.scalalogging.LazyLogging

import java.time.Instant

import upickle.default.{read, write}

import Serializer.given

final class Router(handler: Handler) extends Routes with LazyLogging:
  @cask.get("/now")
  def index() = Response(Instant.now.toString)

  @cask.post("/register")
  def register(request: Request) =
    val register = read[Register](request.text())
    val registered = handler.register(register)
    write[Registered](registered)

  @cask.post("/login")
  def login(request: Request) =
    val login = read[Login](request.text())
    val loggedIn = handler.login(login)
    write[LoggedIn](loggedIn)

  @cask.post("/user/save")
  def saveUser(request: Request) =
    val saveUser = read[SaveUser](request.text())
    val userSaved = handler.saveUser(saveUser)
    write[UserSaved](userSaved)

  @cask.post("/workorder/add")
  def addWorkOrder(request: Request) =
    val addWorkOrder = read[AddWorkOrder](request.text())
    val workOrderAdded = handler.addWorkOrder(addWorkOrder)
    write[WorkOrderAdded](workOrderAdded)

  @cask.post("/workorder/save")
  def saveWorkOrder(request: Request) =
    val saveWorkOrder = read[SaveWorkOrder](request.text())
    val workOrderSaved = handler.saveWorkOrder(saveWorkOrder)
    write[WorkOrderSaved](workOrderSaved)

  initialize()