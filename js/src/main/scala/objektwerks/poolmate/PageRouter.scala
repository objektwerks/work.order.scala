package objektwerks.poolmate

import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.*
import com.raquo.waypoint.*

import upickle.default.*

import org.scalajs.dom

import Serializers.given

object PageRouter:
  given pageRW: ReadWriter[Page] = macroRW
  given poolPageRW: ReadWriter[PoolPage] = macroRW

  val poolRoute = Route[PoolPage, Long](
    encode = poolPage => poolPage.id,
    decode = arg => PoolPage(id = arg),
    pattern = root / "app" / "pool" / segment[Long] / endOfSegments
  )

  val routes = List(
    Route.static(IndexPage, root / endOfSegments),
    Route.static(RegisterPage, root / "register" / endOfSegments),
    Route.static(LoginPage, root / "login" / endOfSegments),
    Route.static(PoolsPage, root / "app" / "pools" / endOfSegments),
    poolRoute,
    Route.static(AccountPage, root / "app" / "account" / endOfSegments)
  )

  val router = new com.raquo.waypoint.Router[Page](
    routes = routes,
    serializePage = page => write(page)(pageRW),
    deserializePage = pageAsString => read(pageAsString)(pageRW),
    getPageTitle = _.title,
  )(
    $popStateEvent = L.windowEvents.onPopState,
    owner = L.unsafeWindowOwner
  )

  val splitter = SplitRender[Page, HtmlElement](router.$currentPage)
    .collectStatic(IndexPage) { IndexView() }
    .collectStatic(RegisterPage) { RegisterView(Model.emailAddressVar) }
    .collectStatic(LoginPage) { LoginView(Model.emailAddressVar, Model.pinVar) }
    .collectStatic(PoolsPage) { PoolsView(Model.pools) }
    .collectStatic(AccountPage) { AccountView(Model.account) }
    .collectSignal[PoolPage] { poolPage => div() }
