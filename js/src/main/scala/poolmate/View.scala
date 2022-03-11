package poolmate

trait View:
  def route(page: Page): Unit = PageRouter.router.pushState(page)