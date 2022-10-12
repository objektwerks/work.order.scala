package poolmate

import scala.collection.mutable

object Validators:
  val licenseInvalid = "A license must be 36 characters."
  val roleInvalid = "A role must be selected."
  val nameInvalid = "A name must be at least 2 characters."
  val emailAddressInvalid = "An email address must be at least 3 characters and inlcude an @ symbol."
  val streetAddressInvalid = "A street address must at least 6 characters."
  val pinInvalid = "A pin must be exactly 7 numbers, characters and/or symbols."
  val datetimeInvalid = "A datetime must be 24-characters, using ISO standard: YYYY-MM-DDTHH:mm:ss.sssZ"
  val idInvalid = "An id must be greater than 0."
  val numberInvalid = "A number must be greater than 0."
  val titleInvalid = "A title must be at least 3 characters and no more than 64 characters."
  val issueInvalid = "An issue must be at least 3 characters and no more than 255 characters."
  val imageUrlInvalid = "An image url must start with /images/"
  val resolutionInvalid = "A resolution must be at least 2 characters and no more than 255 characters."
  val openedInvalid = "An opened date-time value must be 24 characters"

  extension (value: String)
    def isLicense: Boolean = value.length == 36
    def isEmailAddress: Boolean = value.length >= 3 && value.length <= 128 && value.contains("@")
    def isStreetAddress: Boolean = value.length >= 6 && value.length <= 128
    def isPin: Boolean = value.length == 7
    def isName: Boolean = value.length >= 2 && value.length <= 64
    def isRole: Boolean = value == homeowner || value == serviceProvider
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
    def isValid: Array[String] =
      val errors = mutable.ArrayBuilder.make[String]
      errors.result()

  extension (workOrder: WorkOrder)
    def isValid: Array[String] =
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
