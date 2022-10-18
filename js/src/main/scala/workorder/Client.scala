package workorder

import com.raquo.laminar.api.L._

import org.scalajs.dom._
import org.scalajs.dom.console.log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("Client")
class Client(rootUrl: String) extends js.Object:
  Url.root = rootUrl
  Url.now = s"${Url.root}/now"
  Url.register = s"${Url.root}/register"
  Url.login = s"${Url.root}/login"
  Url.userSave = s"${Url.root}/user/save"
  Url.workOrderAdd = s"${Url.root}/workorder/add"
  Url.workOrderSave = s"${Url.root}/workorder/save"
  Url.workOrdersList = s"${Url.root}/workorders"
  Url.command = s"${Url.root}/command"

  log("root url: ", Url.root)
  log("now url: ", Url.now)
  log("register url: ", Url.register)
  log("login url: ", Url.login)
  log("userSave url: ", Url.userSave)
  log("workOrderAdd url: ", Url.workOrderAdd)
  log("workOrderSave url: ", Url.workOrderSave)
  log("workOrdersList url: ", Url.workOrdersList)
  log("command url: ", Url.command)
  
  log(s"server url: http://${window.location.host}")

  Proxy.now.foreach( now => log(s"[now] $now") )

  render(
    container = document.getElementById("container"),
    rootNode = div( child <-- PageRouter.splitter.$view )
  )