package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object Model:
  val emailAddressVar = Var("")
  val pinVar = Var("")
  val account = Var(Account.empty)
  val pools = Pools()

class Pools:
  val emptyPool = Pool()
  val poolsVar = Var(Seq.empty[Pool])
  val poolVar = Var(Pool())
  def set(pools: Seq[Pool]): Pools =
    poolsVar.set(pools)
    this
  def set(id: Long): Pools =
    poolVar.set(poolsVar.now().find(_.id == id).getOrElse(emptyPool))
    this
  def update(pool: Pool): Unit =
    poolsVar.update( _.map( p => if p.id == pool.id then pool else p ) )
    poolVar.set(pool)