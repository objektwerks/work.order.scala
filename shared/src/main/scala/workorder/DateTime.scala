package workorder

import java.time.*
import java.time.format.DateTimeFormatter

object DateTime:
  def now: String = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT )