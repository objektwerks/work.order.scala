package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object Components:
  def btn(label: String): HtmlElement =
    button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo"), label)