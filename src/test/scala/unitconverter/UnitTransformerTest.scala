package unitconverter

import org.specs2.matcher.Matchers
import org.specs2.mutable.Specification
import unitconverter.model.CombineOperation.{Div, Mul}
import unitconverter.model.{ComplexUnit, SingleUnit}

class UnitTransformerTest extends Specification with Matchers {

  import UnitTransformation._

  "UnitTransformer" should {
    "create unit transformer for a minute unit" in {
      val expected = UnitTransformation(60, SingleUnit("s"))
      createTransformer(SingleUnit("minute")) must beRight(expected)
    }

    "create unit transformer for degree/minute" in {
      val input = ComplexUnit(Div, SingleUnit("degree"), SingleUnit("minute"))
      val expectedUnit = ComplexUnit(Div, SingleUnit("rad"), SingleUnit("s"))
      val expectedResult = UnitTransformation(Math.PI/(180*60), expectedUnit)
      createTransformer(input) must beRight(expectedResult)
    }

    "create unit transformer for degree*minute" in {
      val input = ComplexUnit(Mul, SingleUnit("degree"), SingleUnit("minute"))
      val expectedUnit = ComplexUnit(Mul, SingleUnit("rad"), SingleUnit("s"))
      val expectedResult = UnitTransformation(60*Math.PI/180, expectedUnit)
      createTransformer(input) must beRight(expectedResult)
    }

    "create unit transformer for degree*minute*L" in {
      val input = ComplexUnit(Mul, SingleUnit("degree"),
                       ComplexUnit(Mul, SingleUnit("minute"), SingleUnit("L")))

      val expectedUnit = ComplexUnit(Mul, SingleUnit("rad"),
                                ComplexUnit(Mul, SingleUnit("s"), SingleUnit("m\u00B3")))
      val expectedResult = UnitTransformation(60*0.001*Math.PI/180, expectedUnit)
      createTransformer(input) must beRight(expectedResult)
    }

    "fail for unknown unit" in {
      val input = SingleUnit("unknown")
      createTransformer(input) must beLeft()
    }
  }
}
