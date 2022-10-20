package workorder

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
    Route.static(AppPage, root / "app" / endOfSegments),
    Route.static(ProfilePage, root / "profile" / endOfSegments),
    Route.static(WorkOrdersPage, root / "workorders" / endOfSegments)
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
    .collectStatic(RegisterPage) { RegisterView() }
    .collectStatic(AppPage) { AppView() }
    .collectStatic(ProfilePage) { ProfileView(Model.user) }
    .collectStatic(WorkOrdersPage) { WorkOrdersView(Model.workOrders) }