package poolmate

import com.raquo.laminar.api.L.*

import org.scalajs.dom.console.log

object Model:
  val emailAddressVar = Var("")
  val pinVar = Var("")
  val account = Var(Account.empty)
  val pools = EntityModel[Pool](Pool(), Var(Seq.empty[Pool]), Var(Pool()))

final case class EntityModel[E <: Entity](emptyEntity: E,
                                          entitiesVar: Var[Seq[E]],
                                          entityVar: Var[E]):
  given owner: Owner = new Owner {}
  entitiesVar.signal.foreach(entities => log(s"entities change -> ${entities.toString}"))
  entityVar.signal.foreach(entity => log(s"entity change -> ${entity.toString}"))

  def setEntities(entities: Seq[E]): EntityModel[E] =
    entitiesVar.set(entities)
    this

  def setEntity(id: Long): EntityModel[E] =
    entityVar.set(entitiesVar.now().find(_.id == id).getOrElse(emptyEntity))
    this

  def updateEntity(entity: E): Unit =
    entitiesVar.update( _.map( e =>
      if e.id == entity.id then
        entityVar.set(entity)
        entity
      else e
    ))