package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object State:
  val emailAddressVar = Var("")
  val pinVar = Var("")
  val account = Var(Account.empty)
  val pools = Var(Seq.empty[Pool])
  val selectedPool = Var[Pool](Pool())