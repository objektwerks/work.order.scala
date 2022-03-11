package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Error.*
import Validators.*

object PoolView:
  def apply(model: Model[Pool], accountVar: Var[Account]): HtmlElement =
    val nameErrorBus = new EventBus[String]
    val builtErrorBus = new EventBus[String]
    val volumeErrorBus = new EventBus[String]
    val errorBus = new EventBus[String]

    def addHandler(event: Either[Fault, Event]): Unit =
      event match
        case Right(event) =>
          event match
            case Added(pool) =>
              errorBus.emit("")
              // model.addEntity(pool) TODO!
              PageRouter.router.pushState(PoolsPage)
            case _ =>
        case Left(fault) => errorBus.emit(s"Add pool failed: ${fault.cause}")

    def updateHandler(event: Either[Fault, Event]): Unit =
      event match
        case Right(event) =>
          errorBus.emit("")
          PageRouter.router.pushState(PoolsPage)
        case Left(fault) => errorBus.emit(s"Update pool failed: ${fault.cause}")

    div(
      bar(
        btn("Pools").amend {
          onClick --> { _ =>
            log("Pool -> Pools onClick")
            PageRouter.router.pushState(PoolsPage)
          }
        }      
      ),
      div(
        hdr("Pool"),
        lbl("Name"),
        txt.amend {
          value <-- model.selectedEntityVar.signal.map(_.name)
          onInput.mapToValue.filter(_.nonEmpty) --> { name =>
            model.updateSelectedEntity( model.selectedEntityVar.now().copy(name = name) )
          }
          onKeyUp.mapToValue --> { name =>
            if name.nonEmpty then nameErrorBus.emit("") else nameErrorBus.emit(nonEmptyError)
          }
        },
        err(nameErrorBus),
        lbl("Year Built"),
        year.amend {
          value <-- model.selectedEntityVar.signal.map(_.built.toString)
          onInput.mapToValue.filter(_.toIntOption.nonEmpty).map(_.toInt) --> { built =>
            model.updateSelectedEntity( model.selectedEntityVar.now().copy(built = built) )
          }
          onKeyUp.mapToValue.map(_.toInt) --> { built =>
            if built.isGreaterThanZero then builtErrorBus.emit("") else builtErrorBus.emit(nonZeroError)
          }
        },
        err(builtErrorBus),
        lbl("Volume"),
        txt.amend {
          value <-- model.selectedEntityVar.signal.map(_.volume.toString)
          onInput.mapToValue.filter(_.toIntOption.nonEmpty).map(_.toInt) --> { volume =>
            model.updateSelectedEntity( model.selectedEntityVar.now().copy(volume = volume) )
          }
          onKeyUp.mapToValue.map(_.toInt) --> { volume =>
            if volume.isGreaterThanZero then volumeErrorBus.emit("") else volumeErrorBus.emit(nonZeroError)
          }
        },
        err(volumeErrorBus)
      ),
      cbar(
        btn("Add").amend {
          disabled <-- model.selectedEntityVar.signal.map { pool => pool.id.isGreaterThanZero }
          onClick --> { _ =>
            log(s"Pool -> Add onClick")
            val command = AddPool(accountVar.now().license, model.selectedEntityVar.now())
            Proxy.call(command, addHandler)

          }
        },
        btn("Update").amend {
          disabled <-- model.selectedEntityVar.signal.map { pool => pool.id.isZero }
          onClick --> { _ =>
            log(s"Pool -> Update onClick")
            val command = UpdatePool(accountVar.now().license, model.selectedEntityVar.now())
            Proxy.call(command, updateHandler)
          }
        }
      )
    )