package swimmer

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import WeightUnit.*

final class WeightUnitTest extends AnyFunSuite with Matchers:
  test("lbs to kgs"):
    lbsToKgs(1.0) shouldBe 0.454

  test("kgs to lbs"):
    kgsToLbs(1.0) shouldBe 2.205
    kgsToLbs(2.0) shouldBe 4.41