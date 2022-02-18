package poolmate

import com.raquo.laminar.api.L._

import org.scalajs.dom._
import org.scalajs.dom.console.log

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("Client")
class Client(rootUrl: String) extends js.Object:
  Context.rootUrl = rootUrl
  log(s"root url: ${Context.rootUrl}")

  Context.nowUrl = s"$rootUrl/now"
  log(s"now url: ${Context.nowUrl}")

  Context.commandUrl = s"$rootUrl/command"
  log(s"command url: ${Context.commandUrl}")

  render(
    container = document.getElementById("content"),
    rootNode = div( child <-- PageRouter.splitter.$view )
  )