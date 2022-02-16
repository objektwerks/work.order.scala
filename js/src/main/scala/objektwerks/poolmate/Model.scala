package objektwerks.poolmate

import com.raquo.laminar.api.L.*

class Pools:
  val poolsVar = Var(Seq.empty[Pool])
  val poolVar = Var(Pool())
  def update(id: Long): Unit = poolVar.set(poolsVar.now().find(_.id == id).getOrElse(Pool()))
  def update(pool: Pool): Pool =
    poolVar.set(pool)
    poolVar.now()

object Model:
  val emailAddressVar = Var("")
  val pinVar = Var("")
  val account = Var(Account.empty)
  val pools = Pools()