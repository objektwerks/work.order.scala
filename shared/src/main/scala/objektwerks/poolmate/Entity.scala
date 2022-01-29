package objektwerks.poolmate

import java.util.UUID

import scala.util.Random

enum UoM(val abrv: String):
  case ounce extends UoM("oz")
  case gallon extends UoM("gl")
  case pounds extends UoM("lb")

sealed trait Entity

final case class Account(license: String = newLicense,
                         emailAddress: String,
                         pin: String = newPin,
                         activated: Int = DateTime.currentDate,
                         deactivated: Int = 0) extends Entity

object Account:
  private val specialChars = "~!@#$%^&*{}-+<>?/:;".toList
  private val random = new Random

  private def newLicense: String = UUID.randomUUID.toString
  private def newSpecialChar: Char = specialChars(random.nextInt(specialChars.length))
  private def newPin: String =
    Random.shuffle(
      Random
        .alphanumeric
        .take(7)
        .mkString
        .prepended(newSpecialChar)
        .appended(newSpecialChar)
    ).mkString

final case class Email(id: String,
                       license: String,
                       address: String,
                       dateSent: Int = DateTime.currentDate,
                       timeSent: Int = DateTime.currentTime,
                       processed: Boolean = false,
                       valid: Boolean = false) extends Entity

final case class Pool(id: Long = 0,
                      license: String = "",
                      name: String = "",
                      built: Int = 0,
                      volume: Int = 1000) extends Entity

final case class Surface(id: Long = 0,
                         poolId: Long = 0,
                         installed: Int = 0,
                         kind: String = "") extends Entity

final case class Pump(id: Long = 0,
                      poolId: Long = 0,
                      installed: Int = 0,
                      model: String = "") extends Entity

final case class Timer(id: Long = 0,
                       poolId: Long = 0,
                       installed: Int = 0,
                       model: String = "") extends Entity

final case class TimerSetting(id: Long = 0,
                              timerId: Long = 0,
                              created: Int = 0,
                              timeOn: Int = 0,
                              timeOff: Int = 0) extends Entity

final case class Heater(id: Long = 0,
                        poolId: Long = 0,
                        installed: Int = 0,
                        model: String = "") extends Entity

final case class HeaterSetting(id: Long = 0,
                               heaterId: Long = 0,
                               temp: Int = 0,
                               dateOn: Int = 0,
                               dateOff: Int = 0) extends Entity

final case class Measurement(id: Long = 0,
                             poolId: Long = 0,
                             measured: Int = 0,
                             temp: Int = 85,
                             totalHardness: Int = 375,
                             totalChlorine: Int = 3,
                             totalBromine: Int = 5,
                             freeChlorine: Int = 3,
                             ph: Double = 7.4,
                             totalAlkalinity: Int = 100,
                             cyanuricAcid: Long = 50) extends Entity

object Measurement:
  val tempRange = 0 to 100
  val totalHardnessRange = 1 to 1000
  val totalChlorineRange = 0 to 10
  val totalBromineRange = 0 to 20
  val freeChlorineRange = 0 to 10
  val totalAlkalinityRange = 0 to 240
  val cyanuricAcidRange = 0 to 300

final case class Cleaning(id: Long = 0,
                          poolId: Long = 0,
                          cleaned: Int = 0,
                          brush: Boolean = true,
                          net: Boolean = true,
                          vacuum: Boolean = false,
                          skimmerBasket: Boolean = true,
                          pumpBasket: Boolean = false,
                          pumpFilter: Boolean = false,
                          deck: Boolean = false) extends Entity

final case class Chemical(id: Long = 0,
                          poolId: Long = 0,
                          added: Int = 0,
                          chemical: String = "",
                          amount: Double = 0.0,
                          unit: String = "") extends Entity

final case class Supply(id: Long = 0,
                        poolId: Long = 0,
                        purchased: Int = 0,
                        item: String = "",
                        amount: Double = 0.0,
                        unit: String = "",
                        cost: Double = 0.0) extends Entity

final case class Repair(id: Long = 0,
                        poolId: Long = 0,
                        repaired: Int = 0,
                        repair: String = "",
                        cost: Double = 0.0) extends Entity