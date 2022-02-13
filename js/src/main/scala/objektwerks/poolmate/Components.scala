package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object Components:
  val textCss = "w3-input w3-hover-light-gray w3-text-indigo"

  def bar(elms: HtmlElement*): HtmlElement =
    div(cls("w3-bar"), elms)

  def cbar(elms: HtmlElement*): HtmlElement =
    div(cls("w3-bar w3-margin-top w3-center"), elms)

  def btn(text: String): HtmlElement =
    button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo"), text)

  def rbtn(text: String): HtmlElement =
    button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo w3-right"), text)

  def email: Input =
    input(cls(textCss), typ("email"), required(true))

  def lbl(text: String): HtmlElement =
    label(cls("w3-left-align w3-text-indigo"), text)

  def hdr(text: String): HtmlElement =
    h5(cls("w3-light-grey w3-text-indigo"), text)

  def txt: Input =
    input(cls(textCss), required(true))

  def rotext: Input =
    input(cls(textCss), readOnly(true))

  def err(errBus: EventBus[String]): HtmlElement =
    div(cls("w3-border-white w3-text-red"), child.text <-- errBus.events)

  def frm(elms: HtmlElement*): HtmlElement =
    form(cls("w3-container"), elms)