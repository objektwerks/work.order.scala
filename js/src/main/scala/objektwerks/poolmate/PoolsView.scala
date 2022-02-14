package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object PoolsView:
  def apply(pools: Var[Seq[Pool]], selectedPool: Var[Pool]): HtmlElement = div()