package workorder

import com.raquo.laminar.api.L._

import org.scalajs.dom._
import org.scalajs.dom.console.log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("Client")
class Client(rootUrl: String) extends js.Object:
  Urls.set(rootUrl)

  Proxy.now.foreach( now => log(s"[now] $now") )

  render(
    container = document.getElementById("container"),
    rootNode = div( child <-- PageRouter.splitter.$view )
  )