package poolmate

object Validators:
  val licenseInvalidMessage = "A license must be 36 characters."
  val roleInvalidMessage = "A role must be selected."
  val nameInvalidMessage = "A name must be at least 2 characters."
  val emailAddressInvalidMessage = "An email address must be at least 3 characters and inlcude an @ symbol."
  val streetAddressInvalidMessage = "A street address must at least 6 characters."
  val pinInvalidMessage = "A pin must be exactly 7 numbers, characters and/or symbols."
  val datetimeInvalidMessage = "A datetime must be 24-characters, using ISO standard: YYYY-MM-DDTHH:mm:ss.sssZ"
  val idInvalidMessage = "An id must be greater than 0."
  val numberInvalidMessage = "A number must be greater than 0."

  extension (value: String)
    def isLicense: Boolean = value.length == 36
    def isEmailAddress: Boolean = value.length >= 3 && value.length <= 128 && value.contains("@")
    def isStreetAddress: Boolean = value.length >= 6 && value.length <= 128
    def isPin: Boolean = value.length == 7
    def isName: Boolean = value.length >= 2 && value.length <= 64
    def isRole: Boolean = value == homeowner || value == serviceProvider
    def isImageUrl: Boolean = value.startsWith("/images/")

  extension (value: Int)
    def isZero: Boolean = value == 0
    def isGreaterThanZero: Boolean = value > 0
    def isGreaterThanOrEqualZero = value >= 0

  extension (user: User)
    def isValid: List[String] =
      val errors = List[String]()
      errors

  extension (workOrder: WorkOrder)
    def isValid: List[String] =
      val errors = List[String]()
      errors
