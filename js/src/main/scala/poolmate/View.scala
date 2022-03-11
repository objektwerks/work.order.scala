package poolmate

import com.raquo.laminar.api.L.*

trait View:
  val errorBus = new EventBus[String]

  def call(command: Command, handler: (event: Either[Fault, Event]) => Unit): Unit = Proxy.call(command, handler)
  
  def route(page: Page): Unit = PageRouter.router.pushState(page)

  def clearErrors(): Unit = errorBus.emit("")