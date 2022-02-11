package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object Components:
  def btn(text: String): HtmlElement =
    button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo"), text)

  def rbtn(text: String): HtmlElement =
    button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo w3-right"), text)

  def lbl(text: String): HtmlElement =
    label(cls("w3-left-align w3-text-indigo"), text)