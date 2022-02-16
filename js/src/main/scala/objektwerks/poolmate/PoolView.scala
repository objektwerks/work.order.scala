package objektwerks.poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Errors.*

object PoolView:
  def apply(model: Pools, id: Long): HtmlElement =
    model.set(id)
    val nameErrors = new EventBus[String]
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
          value <-- model.poolVar.signal.map(_.license)
        },
        lbl("Name"),
        txt.amend {
          value <-- model.poolVar.signal.map(_.name)
          onInput.mapToValue.filter(_.nonEmpty) --> { name =>
            model.update( model.poolVar.now().copy(name = name) )
          }
          onKeyUp.mapToValue --> { name =>
            if name.nonEmpty then nameErrors.emit("") else nameErrors.emit(nonEmptyError)
          }
        },
        err(nameErrors)
      )
    )