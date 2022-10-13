package poolmate

final class Service(store: Store):
  def register(register: Register): Either[Throwable, Registered] = Left("todo")
  def login(login: Login): Either[Throwable, LoggedIn] = Left("todo")