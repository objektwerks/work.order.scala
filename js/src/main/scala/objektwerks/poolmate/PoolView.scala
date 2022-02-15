package objektwerks.poolmate

object PoolView:
  def apply(id: Long): HtmlElement =
    div(
      bar(
        btn("Pools").amend {
          onClick --> { _ =>
            log("Pools onClick")
            PageRouter.router.pushState(PoolsPage)
          }
        }      
      ),
      div(
        hdr("Pool")
      )
    )