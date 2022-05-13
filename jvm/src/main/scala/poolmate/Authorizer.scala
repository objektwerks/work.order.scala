package poolmate

import Validators.*

final class Authorizer(service: Service):
  def authorize(command: Command): Event =
    command match
      case license: License => service.authorize(license.license)
      case Join(_) | Enter(_, _) => Authorized("")