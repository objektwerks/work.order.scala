package objektwerks.service

import objektwerks.entity.*

class Dispatcher(authorizer: Authorizer, validator: Validator, handler: Handler):
  def dispatch(command: Command): Event =
    authorizer.authorize(command) match
      case unauthorized: Unauthorized => unauthorized
      case _ => 
        if validator.validate(command) then handler.handle(command)
        else Fault(s"Invalid command: $command")