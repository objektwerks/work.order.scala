package workorder

import org.scalajs.dom._
import org.scalajs.dom.console.log

object Urls:
  var root = ""
  var now = ""
  var register = ""
  var login = ""
  var userSave = ""
  var workOrderAdd = ""
  var workOrderSave = ""
  var workOrdersList = ""
  var command = ""

  def set(rootUrl: String): Unit =
    root = rootUrl
    now = s"$root/now"
    register = s"$root/register"
    login = s"$root/login"
    userSave = s"$root/user/save"
    workOrderAdd = s"$root/workorder/add"
    workOrderSave = s"$root/workorder/save"
    workOrdersList = s"$root/workorders"
    command = s"$root/command"

    log("root url: ", root)
    log("now url: ", now)
    log("register url: ", register)
    log("login url: ", login)
    log("userSave url: ", userSave)
    log("workOrderAdd url: ", workOrderAdd)
    log("workOrderSave url: ", workOrderSave)
    log("workOrdersList url: ", workOrdersList)
    log("command url: ", command)
    log(s"server url: http://${window.location.host}")