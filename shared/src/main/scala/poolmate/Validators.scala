package poolmate

object Validators:
  val licenseInvalidMessage = "A valid license is 36 characters."
  val roleInvalidMessage = "A valid role must be selected."
  val nameInvalidMessage = "For name, enter at least 2 characters."
  val emailAddressInvalidMessage = "For email address, enter at least 3 characters to inlcude @."
  val streetAddressInvalidMessage = "For street address, enter at least 6 characters."
  val pinInvalidMessage = "For pin, enter exactly 7 numbers, characters and/or symbols."
  val datetimeInvalidMessage = "For datetime, must use 24-character ISO standard: YYYY-MM-DDTHH:mm:ss.sssZ"
  val idInvalidMessage = "An id must be greater than 0."
  val numberInvalidMessage = "A number must be greater than 0."
  val definedInvalidMessage = "This field may be empty, but must be defined."

  extension (value: String)
    def isLicense: Boolean = if value.nonEmpty then value.length == 36 else false
    def isEmailAddress: Boolean = value.nonEmpty && value.length >= 3 && value.contains("@")
    def isPin: Boolean = value.length == 7
    def isName: Boolean = value.length >= 2 && value.length <= 24

  extension (value: Int)
    def isGreaterThan1899 = value > 1899
    def isGreaterThan999 = value > 999
    def isZero: Boolean = value == 0
    def isGreaterThanZero: Boolean = value > 0

  extension (user: User)
    def isValid: List[String] = List()

  extension (workOrder: WorkOrder)
    def isValid: List[String] = List()
