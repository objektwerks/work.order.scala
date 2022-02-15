package objektwerks.poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object PoolView:
  def apply(id: Long): HtmlElement =
    div(
      bar(
        btn("Pools").amend {
          onClick --> { _ =>
            log("Pools onClick")
            PageRouter.router.pushState(PoolsPage)
          }
        }      
      ),
      div(
        hdr("Pool")
      )
    )