package swimmer

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

final class PinTest extends AnyFunSuite with Matchers:
  test("pin"):
    for i <- 1 to 1000 yield Pin.newInstance.length shouldBe 7