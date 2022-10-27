package workorder

import java.time.*
import java.time.format.DateTimeFormatter

object DateTime:
  var zoneId = ZoneId.of("UTC", ZoneId.SHORT_IDS)
  zoneId = if zoneId == null then ZoneId.of("UTC") else zoneId

  val dateFormatter = format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
  val timeFormatter = format.DateTimeFormatter.ofPattern("HH:mm")

  def currentDate: Int = localDateToInt(LocalDate.now(zoneId))

  def localDateToInt(localDate: LocalDate): Int =
    localDateToString(localDate).replace("-", "").toInt

  def localDateToString(localDate: LocalDate): String =
    localDate.format(dateFormatter)

  def currentTime: Int = localTimeToInt(LocalTime.now(zoneId))

  def localTimeToInt(localTime: LocalTime): Int =
    localTimeToString(localTime).replace(":", "").toInt

  def localTimeToString(localTime: LocalTime): String =
    localTime.format(timeFormatter)

  def now: String = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT )

  def nano: Int = Instant.now().getNano