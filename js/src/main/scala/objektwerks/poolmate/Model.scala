package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object Model:
  val emailAddressVar = Var("")
  val pinVar = Var("")
  val account = Var(Account.empty)
  val pools = Entities[Pool](Pool(), Var(Seq.empty[Pool]), Var(Pool()))

final case class Entities[E <: Entity](emptyEntity: E,
                                       entitiesVar: Var[Seq[E]],
                                       entityVar: Var[E]):
  def setEntities(entities: Seq[E]): Entities[E] =
    entitiesVar.set(entities)
    this
  def setEntity(id: Long): Entities[E] =
    entityVar.set(entitiesVar.now().find(_.id == id).getOrElse(emptyEntity))
    this
  def update(entity: E): Unit =
    entitiesVar.update( _.map( e =>
      if e.id == entity.id then
        entityVar.set(entity)
        entity
      else e
    ))