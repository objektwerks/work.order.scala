package poolmate

import java.util.UUID

import scala.util.Random

val homeowner = "homeowner"
val serviceProvider = "serviceprovider"

sealed trait Entity

object User:
  private val specialChars = "~!@#$%^&*-+=<>?/:;".toList
  private val random = new Random

  private def newSpecialChar: Char = specialChars(random.nextInt(specialChars.length))

  /**
   * 26 letters + 10 numbers + 18 special characters = 54 combinations
   * 7 alphanumeric char pin = 54^7 ( 1,338,925,209,984 )
   */
  def newPin: String =
    Random.shuffle(
      Random
        .alphanumeric
        .take(5)
        .mkString
        .prepended(newSpecialChar)
        .appended(newSpecialChar)
    ).mkString

  def newLicense: String = UUID.randomUUID.toString

  def empty: User = User(0, "", "", "", "", "", "", "")

final case class User(id: Int,
                      role: String,
                      name: String,
                      emailAddress: String,
                      streetAddress: String,
                      registered: String,
                      pin: String,
                      license: String) extends Entity

final case class WorkOrder(number: Int,
                           homeownerId: Int,
                           serviceProviderId: Int,
                           title: String,
                           issue: String,
                           streetAddress: String,
                           imageUrl: String,
                           resolution: String,
                           opened: String,
                           closed: String) extends Entity