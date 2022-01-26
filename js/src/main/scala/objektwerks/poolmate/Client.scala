package objektwerks.poolmate

import com.raquo.laminar.api.L._

import org.scalajs.dom._
import org.scalajs.dom.console.log

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("Client")
class Client(publicUrl: String, apiUrl: String) extends js.Object:
  log(s"public url: $publicUrl api url: $apiUrl")
  render(document.getElementById("content"), h3("content"))