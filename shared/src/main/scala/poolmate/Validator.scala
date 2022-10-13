package poolmate

import scala.collection.mutable

object Validator:
  val idInvalid = "An id must be greater than 0."
  val roleInvalid = "A role must be selected."
  val nameInvalid = "A name must be at least 2 characters."
  val emailAddressInvalid = "An email address must be at least 3 characters and inlcude an @ symbol."
  val streetAddressInvalid = "A street address must at least 6 characters."
  val registeredInvalid = "An registered date-time value must be 24 characters."
  val pinInvalid = "A pin must be exactly 7 numbers, characters and/or symbols."
  val licenseInvalid = "A license must be 36 characters."
  val numberInvalid = "A number must be greater than 0."
  val titleInvalid = "A title must be at least 3 characters."
  val issueInvalid = "An issue must be at least 3 characters."
  val imageUrlInvalid = "An image url must start with /images/"
  val resolutionInvalid = "A resolution must be at least 2 characters."
  val openedInvalid = "An opened date-time value must be 24 characters."

  extension (value: String)
    def isName: Boolean = value.length >= 2 && value.length <= 64
    def isRole: Boolean = value == Roles.homeowner || value == Roles.serviceProvider
    def isEmailAddress: Boolean = value.length >= 3 && value.length <= 128 && value.contains("@")
    def isStreetAddress: Boolean = value.length >= 6 && value.length <= 128
    def isRegistered: Boolean = value.length == 24
    def isPin: Boolean = value.length == 7
    def isLicense: Boolean = value.length == 36
    def isTitle: Boolean = value.length >= 3 && value.length <= 64
    def isIssue: Boolean = value.length >= 3 && value.length <= 255
    def isResolution: Boolean = value.length >= 2 && value.length <= 255
    def isImageUrl: Boolean = value.startsWith("/images/")
    def isOpened: Boolean = value.length == 24
    def isClosed: Boolean = value.length == 24

  extension (value: Int)
    def isZero: Boolean = value == 0
    def isGreaterThanZero: Boolean = value > 0
    def isNotGreaterThanZero: Boolean = value <= 0
    def isGreaterThanOrEqualZero = value >= 0

  extension (user: User)
    def isValid: Boolean = user.validate.isEmpty
    def validate: Array[String] =
      val errors = mutable.ArrayBuilder.make[String]
      if user.id.isNotGreaterThanZero then errors += idInvalid
      if !user.role.isRole then errors += roleInvalid
      if !user.name.isName then errors += nameInvalid
      if !user.emailAddress.isEmailAddress then errors += emailAddressInvalid
      if !user.streetAddress.isStreetAddress then errors += streetAddressInvalid
      if !user.registered.isRegistered then errors += registeredInvalid
      if !user.pin.isPin then errors += pinInvalid
      if !user.license.isLicense then errors += licenseInvalid
      errors.result()

  extension (workOrder: WorkOrder)
    def isValid: Boolean = workOrder.validate.isEmpty
    def validate: Array[String] =
      val errors = mutable.ArrayBuilder.make[String]
      if workOrder.number.isNotGreaterThanZero then errors += numberInvalid
      if workOrder.homeownerId.isNotGreaterThanZero then errors += idInvalid
      if workOrder.serviceProviderId.isNotGreaterThanZero then errors += idInvalid
      if !workOrder.title.isTitle then errors += titleInvalid
      if !workOrder.issue.isIssue then errors += issueInvalid
      if !workOrder.streetAddress.isStreetAddress then errors += streetAddressInvalid
      if !workOrder.imageUrl.isImageUrl then errors += imageUrlInvalid
      if !workOrder.resolution.isResolution then errors += resolutionInvalid
      if !workOrder.opened.isOpened then errors += openedInvalid
      errors.result()

  extension (register: Register)
    def isValid: Boolean = register.validate.isEmpty
    def validate: Array[String] =
      val errors = mutable.ArrayBuilder.make[String]
      if !register.role.isRole then errors += roleInvalid
      if !register.name.isName then errors += nameInvalid
      if !register.emailAddress.isEmailAddress then errors += emailAddressInvalid
      if !register.streetAddress.isStreetAddress then errors += streetAddressInvalid
      errors.result()

  extension (login: Login)
    def isValid: Boolean = login.validate.isEmpty
    def validate: Array[String] =
      val errors = mutable.ArrayBuilder.make[String]
      if !login.emailAddress.isEmailAddress then errors += emailAddressInvalid
      if !login.pin.isPin then errors += pinInvalid
      errors.result()