package poolmate

import com.raquo.laminar.api.L._

import org.scalajs.dom._
import org.scalajs.dom.console.log

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("Client")
class Client(rootUrl: String) extends js.Object:
  Url.root = rootUrl
  log(s"root url: ${Url.root}")

  Url.now = s"${Url.root}/now"
  log(s"now url: ${Url.now}")

  Url.command = s"${Url.root}/command"
  log(s"command url: ${Url.command}")

  render(
    container = document.getElementById("content"),
    rootNode = div( child <-- PageRouter.splitter.$view )
  )