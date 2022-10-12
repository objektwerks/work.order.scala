package poolmate

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PinTest extends AnyFunSuite with Matchers:
  test("pin") {
    User.newPin.length shouldBe 7
  }