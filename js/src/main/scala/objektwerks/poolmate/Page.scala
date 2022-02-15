package objektwerks.poolmate

import com.raquo.laminar.api.L.*

sealed trait Page:
  val title = "Poolmate"

case object IndexPage extends Page
case object RegisterPage extends Page
case object LoginPage extends Page

case object PoolsPage extends Page
case class PoolPage(id: Long) extends Page

case object AccountPage extends Page