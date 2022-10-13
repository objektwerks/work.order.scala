package poolmate

import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.*
import com.raquo.waypoint.*

import upickle.default.*

import Serializer.given

object PageRouter:
  given pageRW: ReadWriter[Page] = macroRW

  val routes = List(
    Route.static(RootPage, root / endOfSegments),
    Route.static(RegisterPage, root / "register" / endOfSegments),
    Route.static(LoginPage, root / "login" / endOfSegments),
    Route.static(UserPage, root / "user" / endOfSegments),
    Route.static(WorkOrdersPage, root / "workorders" / endOfSegments),
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
    .collectStatic(RootPage) { RootView() }
    .collectStatic(LoginPage) { LoginView() }
    .collectStatic(UserPage) { UserView(Model.user) }
    .collectStatic(WorkOrdersPage) { WorkOrdersView(Model.pools, Model.accountVar) }
    .collect[PoolPage] { page => PoolView(Model.pools.setSelectedEntityById(page.id), Model.accountVar) }