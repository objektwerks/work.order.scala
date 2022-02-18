package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Error.*
import Validators.*

object PoolView:
  def apply(model: EntityModel[Pool]): HtmlElement =
    val nameErrors = new EventBus[String]
    val builtErrors = new EventBus[String]
    val volumeErrors = new EventBus[String]
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
        lbl("Name"),
        txt.amend {
          value <-- model.entityVar.signal.map(_.name)
          onInput.mapToValue.filter(_.nonEmpty) --> { name =>
            model.updateEntity( model.entityVar.now().copy(name = name) )
          }
          onKeyUp.mapToValue --> { name =>
            if name.nonEmpty then nameErrors.emit("") else nameErrors.emit(nonEmptyError)
          }
        },
        err(nameErrors),
        lbl("Built"),
        txt.amend {
          value <-- model.entityVar.signal.map(_.built.toString)
          onInput.mapToValue.filter(_.toIntOption.nonEmpty).map(_.toInt) --> { built =>
            model.updateEntity( model.entityVar.now().copy(built = built) )
          }
          onKeyUp.mapToValue.map(_.toInt) --> { built =>
            if built.isGreaterThanZero then builtErrors.emit("") else builtErrors.emit(nonZeroError)
          }
        },
        err(builtErrors),
        lbl("Volume"),
        txt.amend {
          value <-- model.entityVar.signal.map(_.volume.toString)
          onInput.mapToValue.filter(_.toIntOption.nonEmpty).map(_.toInt) --> { volume =>
            model.updateEntity( model.entityVar.now().copy(volume = volume) )
          }
          onKeyUp.mapToValue.map(_.toInt) --> { volume =>
            if volume.isGreaterThanZero then volumeErrors.emit("") else volumeErrors.emit(nonZeroError)
          }
        },
        err(volumeErrors)
      ),
      cbar(
        btn("Add").amend {
          disabled <-- model.entityVar.signal.map { pool => !(pool.id.isZero && pool.isValid) }
          onClick --> { _ =>
            log(s"Add onClick")
            PageRouter.router.pushState(PoolsPage)
          }
        },
        btn("Update").amend {
          disabled <-- model.entityVar.signal.map { pool => !(pool.id.isGreaterThanZero && pool.isValid) }
          onClick --> { _ =>
            log(s"Update onClick")
            PageRouter.router.pushState(PoolsPage)
          }
        }
      )
    )