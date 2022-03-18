package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

import Components.*
import Error.*
import Validators.*

object PoolView extends View:
  def apply(model: Model[Pool], accountVar: Var[Account]): HtmlElement =
    val nameErrorBus = new EventBus[String]
    val builtErrorBus = new EventBus[String]
    val volumeErrorBus = new EventBus[String]

    def addHandler(event: Either[Fault, Event]): Unit =
      event match
        case Right(event) =>
          event match
            case PoolAdded(pool) =>
              clearErrors()
              model.addEntity(pool)
              route(PoolsPage)
            case _ => log(s"Pool view add handler failed: $event")
        case Left(fault) => errorBus.emit(s"Add pool failed: ${fault.cause}")

    def updateHandler(event: Either[Fault, Event]): Unit =
      event match
        case Right(event) =>
          event match
            case Updated() =>
              clearErrors()
              route(PoolsPage)
            case _ => log(s"Pool view update handler failed: $event")
        case Left(fault) => errorBus.emit(s"Update pool failed: ${fault.cause}")

    div(
      bar(
        btn("Pools").amend {
          onClick --> { _ =>
            log("Pool -> Pools menu item onClick")
            route(PoolsPage)
          }
        },
        dropdown(
          btn("Hardware"), // Surfaces, Decks, Pumps, Timers, Heaters
          btn("Surfaces").amend {
            onClick --> { _ =>
              log("Hardware -> Surfaces menu item onClick")
            }
          },
          btn("Decks").amend {
            onClick --> { _ =>
              log("Hardware -> Decks menu item onClick")
            }
          },
          btn("Pumps").amend {
            onClick --> { _ =>
              log("Hardware -> Pumps menu item onClick")
            }
          },
          btn("Timers").amend {
            onClick --> { _ =>
              log("Hardware -> Timers menu item onClick")
            }
          }
        ),
        dropdown(
          btn("Maintenance"), // Measurements, Cleanings, Chemicals
          btn("Measurements").amend {
            onClick --> { _ =>
              log("Maintenance -> Measurements menu item onClick")
            }
          }
        ),
        dropdown(
          btn("Expenses"), // Supplies, Repairs
          btn("Supplies").amend {
            onClick --> { _ =>
              log("Expenses -> Supplies menu item onClick")
            }
          }
        )         
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
            if name.isName then clear(nameErrorBus) else emit(nameErrorBus, nameError)
          }
        },
        err(nameErrorBus),
        lbl("Built"),
        year.amend {
          value <-- model.selectedEntityVar.signal.map(_.built.toString)
          onInput.mapToValue.filter(_.toIntOption.nonEmpty).map(_.toInt) --> { built =>
            model.updateSelectedEntity( model.selectedEntityVar.now().copy(built = built) )
          }
          onKeyUp.mapToValue.map(_.toInt) --> { built =>
            if built.isGreaterThan1899 then clear(builtErrorBus) else emit(builtErrorBus, builtError)
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
            if volume.isGreaterThan999 then clear(volumeErrorBus) else emit(volumeErrorBus, volumeError)
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
            call(command, addHandler)

          }
        },
        btn("Update").amend {
          disabled <-- model.selectedEntityVar.signal.map { pool => pool.id.isZero }
          onClick --> { _ =>
            log(s"Pool -> Update onClick")
            val command = UpdatePool(accountVar.now().license, model.selectedEntityVar.now())
            call(command, updateHandler)
          }
        }
      )
    )