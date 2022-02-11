package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object Components:
  def bar(btns: HtmlElement*): HtmlElement =
    div(cls("w3-bar"), btns)

  def btn(text: String): HtmlElement =
    button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo"), text)

  def rbtn(text: String): HtmlElement =
    button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo w3-right"), text)

  def lbl(text: String): HtmlElement =
    label(cls("w3-left-align w3-text-indigo"), text)

  def hdr(text: String): HtmlElement =
    h5(cls("w3-light-grey w3-text-indigo"), text)

  def txt: HtmlElement =
    input(cls("w3-input w3-hover-light-gray w3-text-indigo"))

  def err(errBus: EventBus[String]): HtmlElement =
    div(cls("w3-border-white w3-text-red"), child.text <-- errBus.events)