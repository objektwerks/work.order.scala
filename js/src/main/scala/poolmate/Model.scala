package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

object Model:
  val emailAddressVar = Var("")
  val pinVar = Var("")
  val account = Var(Account.empty)
  val pools = EntitiesModel[Pool](Var(Seq.empty[Pool]), Var(Pool()), Pool())

final case class EntitiesModel[E <: Entity](entitiesVar: Var[Seq[E]],
                                            currentEntityVar: Var[E],
                                            emptyEntity: E):
  given owner: Owner = new Owner {}
  entitiesVar.signal.foreach(entities => log(s"entities var change -> ${entities.toString}"))
  currentEntityVar.signal.foreach(entity => log(s"current entity var change -> ${entity.toString}"))

  def setCurrentEntity(id: Long): EntitiesModel[E] =
    currentEntityVar.set(entitiesVar.now().find(_.id == id).getOrElse(emptyEntity))
    this

  def updateCurrentEntity(entity: E): Unit =
    entitiesVar.update( _.map( e =>
      if e.id == entity.id then
        currentEntityVar.set(entity)
        entity
      else e
    ))