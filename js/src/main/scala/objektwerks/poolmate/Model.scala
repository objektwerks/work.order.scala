package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object Model:
  val emailAddressVar = Var("")
  val pinVar = Var("")
  val account = Var(Account.empty)
  val pools = Var(Seq.empty[Pool])