package poolmate

final class Dispatcher(validator: Validator,
                       service: Service,
                       emailer: Emailer):
  def dispatch(command: Command): Event =
    command match
      case register: Register =>
        emailer.send(register).fold(_ => Registered(s"Invalid email address: ${register.emailAddress}"), registered => registered)
      case login: Login =>
        service.enter(login.emailAddress, login.pin).fold(throwable => LoggedIn(throwable.getMessage()), loggedIn => loggedIn)