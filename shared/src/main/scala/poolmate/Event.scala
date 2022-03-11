package poolmate

sealed trait Event

final case class Authorized(license: String) extends Event
final case class Unauthorized(license: String) extends Event

final case class Registering() extends Event
final case class LoggedIn(account: Account) extends Event

final case class Deactivated(account: Account) extends Event
final case class Reactivated(account: Account) extends Event

final case class Listed(entities: Seq[Entity]) extends Event
final case class Added(entity: Entity) extends Event
final case class Updated() extends Event

final case class PoolsListed(pools: Seq[Pool]) extends Event
final case class PoolAdded(pool: Pool) extends Event

final case class SurfacesListed(surfaces: Seq[Surface]) extends Event
final case class SurfaceAdded(surface: Surface) extends Event

final case class PumpsListed(pumps: Seq[Pump]) extends Event
final case class PumpAdded(pump: Pump) extends Event

final case class TimersListed(timers: Seq[Timer]) extends Event
final case class TimerAdded(timer: Timer) extends Event

final case class TimerSettingsListed(timerSettings: Seq[TimerSetting]) extends Event
final case class TimerSettingAdded(timerSetting: TimerSetting) extends Event

final case class HeatersListed(heaters: Seq[Heater]) extends Event
final case class HeaterAdded(heater: Heater) extends Event

final case class Fault(dateOf: Int = DateTime.currentDate,
                       timeOf: Int = DateTime.currentTime,
                       nanoOf: Int = DateTime.nano,
                       cause: String) extends Event

object Fault:
  def apply(message: String): Fault = Fault(cause = message)
  def apply(throwable: Throwable): Fault = Fault(cause = throwable.getMessage)