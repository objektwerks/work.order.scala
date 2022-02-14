package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object AccountView:
  def apply(account: Var[Account]): HtmlElement = div()