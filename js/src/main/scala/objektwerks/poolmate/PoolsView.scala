package objektwerks.poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*

object PoolsView:
  def apply(pools: Var[Seq[Pool]], selectedPool: Var[Pool]): HtmlElement =
    def split: Signal[Seq[Li]] = pools.signal.split(_.id)( (id, _, poolSignal) =>
      toLi(poolSignal.map(_.name)).amend {
        onClick --> { _ =>
          pools.now().find(_.id == id).foreach { pool =>
            selectedPool.set(pool)
            //Todo show pool view
          }
        }
      }
    )
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
        list(split)
      )
    )