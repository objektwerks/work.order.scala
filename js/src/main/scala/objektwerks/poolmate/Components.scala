package objektwerks.poolmate

import com.raquo.laminar.api.L.*

object Components:
  private val inputCss = "w3-input w3-hover-light-gray w3-text-indigo"

  def bar(elms: HtmlElement*): Div = div(cls("w3-bar"), elms)

  def cbar(elms: HtmlElement*): Div = div(cls("w3-bar w3-margin-top w3-center"), elms)

  def btn(text: String): Button = button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo"), text)

  def rbtn(text: String): HtmlElement = button(cls("w3-button w3-round-xxlarge w3-light-grey w3-text-indigo w3-right"), text)
  
  def lbl(text: String): Label = label(cls("w3-left-align w3-text-indigo"), text)

  def txt: Input = input(cls(inputCss), required(true))

  def rotxt: Input = input(cls(inputCss), readOnly(true))

  def email: Input = input(cls(inputCss), typ("email"), minLength(3), placeholder("address@email.com"), required(true))

  def pin: Input = input(cls(inputCss), typ("text"), minLength(9), maxLength(9), required(true), placeholder("a1b2c3d4e"))

  def int: Input = input(cls(inputCss), typ("number"), pattern("\\d*"), stepAttr("1"), required(true))

  def dbl: Input = input(cls(inputCss), typ("number"), pattern("[0-9]+([.,][0-9]+)?"), stepAttr("0.01"), required(true))

  def hdr(text: String): HtmlElement = h5(cls("w3-light-grey w3-text-indigo"), text)

  def err(errBus: EventBus[String]): Div = div(cls("w3-border-white w3-text-red"), child.text <-- errBus.events)

  def list(itemsSignal: Signal[Seq[Li]]): Div = div(cls("w3-container"), ul(cls("w3-ul w3-hoverable"), children <-- itemsSignal))

  def render(itemSignal: Signal[String]): Li = li(cls("w3-text-indigo w3-display-container"), child.text <-- itemSignal)