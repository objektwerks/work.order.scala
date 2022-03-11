package poolmate

trait View:
  def call(command: Command, handler: (event: Either[Fault, Event]) => Unit): Unit = Proxy.call(command, handler)
  
  def route(page: Page): Unit = PageRouter.router.pushState(page)