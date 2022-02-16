package objektwerks.poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Errors.*

object PoolView:
  def apply(poolsVar: Var[Seq[Pool]], id: Long): HtmlElement =
    val poolVar = Var(poolsVar.now().find(_.id == id).getOrElse(Pool()))
    val nameErrors = new EventBus[String]
    def update(pool: Pool): Pool =
      poolVar.set(pool)
      poolVar.now()
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
        hdr("Pool"),
        lbl("License"),
        rotxt.amend {
          value <-- poolVar.signal.map(_.license)
        },
        lbl("Name"),
        txt.amend {
          value <-- poolVar.signal.map(_.name)
          onInput.mapToValue.filter(_.nonEmpty) --> { name =>
            poolsVar.update( _.map( pool => if pool.id == id then update(pool.copy(name = name)) else pool ))
          }
          onKeyUp.mapToValue --> { name =>
            if name.nonEmpty then nameErrors.emit("") else nameErrors.emit(nonEmptyError)
          }
        },
        err(nameErrors)
      )
    )