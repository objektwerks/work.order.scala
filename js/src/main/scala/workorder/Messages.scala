package workorder

trait Messages:
  val emailAddressError = "Enter a valid email address."
  val pinError = "Enter a valid pin of 7 alphanumeric characters."
  val nameError = "Enter a 2 characters or more."
  val builtError = "Enter a year greater than 1900."
  val volumeError = "Enter a volume greater than 999."

  val registerMessage = "Upon success, you'll transition to the Login page. Record your pin! You'll also receive an email."
  val pinMessage = "Keep your pin of 7 alphanumeric characters in a secure place!"