package objektwerks.poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object PoolsView:
  def apply(model: Pools): HtmlElement =
    div(
      bar(
        btn("Account").amend {
          onClick --> { _ =>
            log("Account onClick")
            PageRouter.router.pushState(AccountPage)
          }
        }      
      ),
      div(
        hdr("Pools"),
        list(
          split(model.poolsVar, (id: Long) => PoolPage(id))
        )
      )
    )