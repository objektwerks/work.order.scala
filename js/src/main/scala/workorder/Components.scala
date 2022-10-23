package workorder

import com.raquo.laminar.api.L.*

import scala.scalajs.js.Date

object Components:
  private val inputCss = "w3-input w3-hover-light-gray w3-text-indigo"

  def bar(elms: HtmlElement*): Div =
    div(cls("w3-bar"), elms)

  def cbar(elms: HtmlElement*): Div =
    div(cls("w3-bar w3-margin-top w3-center"), elms)

  def btn(text: String): Button =
    button(cls("w3-button w3-round w3-indigo"), text)

  def rbtn(text: String): Button =
    button(cls("w3-button w3-round w3-indigo w3-right"), text)

  def lbl(text: String): Label =
    label(cls("w3-left-align w3-text-indigo"), text)

  def txt: Input =
    input(cls(inputCss), required(true))

  def rotxt: Input =
    input(cls("w3-input w3-light-gray w3-text-indigo"), readOnly(true))

  def email: Input =
    input(cls(inputCss), typ("email"), minLength(3), required(true))

  def pin: Input =
    input(cls(inputCss), typ("text"), minLength(7), maxLength(7), required(true))

  def int: Input =
    input(cls(inputCss), typ("number"), pattern("\\d*"), stepAttr("1"), required(true))

  def dbl: Input =
    input(cls(inputCss), typ("number"), pattern("[0-9]+([.,][0-9]+)?"), stepAttr("0.01"), required(true))

  def hdr(text: String): HtmlElement =
    h5(cls("w3-indigo"), text)

  def err(errBus: EventBus[String]): Div =
    div(cls("w3-red"), child.text <-- errBus.events)