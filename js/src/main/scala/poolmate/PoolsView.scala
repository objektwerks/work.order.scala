package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object PoolsView extends View:
  def apply(model: Model[Pool]): HtmlElement =
    div(
      bar(
        btn("Account").amend {
          onClick --> { _ =>
            log("Pools -> Account menu item onClick")
            PageRouter.router.pushState(AccountPage)
          }
        }      
      ),
      div(
        hdr("Pools"),
        list(
          split(model.entitiesVar, (id: Long) => PoolPage(id))
        )
      ),
      cbar(
        btn("Add").amend {
          onClick --> { _ =>
            log(s"Pools -> Add onClick")
            PageRouter.router.pushState(PoolPage())
          }
        }
      )
    )