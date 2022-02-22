package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

object Model:
  val emailAddressVar = Var("")
  val pinVar = Var("")
  val account = Var(Account.empty)
  val pools = EntitiesModel[Pool](Var(Seq.empty[Pool]), Var(Pool()), Pool())

final case class EntitiesModel[E <: Entity](entitiesVar: Var[Seq[E]],
                                            selectedEntityVar: Var[E],
                                            emptyEntity: E):
  given owner: Owner = new Owner {}
  entitiesVar.signal.foreach(entities => log(s"entities model -> ${entities.toString}"))
  selectedEntityVar.signal.foreach(entity => log(s"selected entity -> ${entity.toString}"))

  def setSelectedEntityById(id: Long): EntitiesModel[E] =
    selectedEntityVar.set(entitiesVar.now().find(_.id == id).getOrElse(emptyEntity))
    this

  def updateSelectedEntity(entity: E): Unit =
    entitiesVar.update { entities =>
      entities.map( e =>
        if e.id == entity.id then
          selectedEntityVar.set(entity)
          entity
        else e
      )
    }