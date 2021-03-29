package unitconverter

import org.specs2.matcher.Matchers
import org.specs2.mutable.Specification
import unitconverter.model.CombineOperation.{Div, Mul}
import unitconverter.model.{ComplexUnit, SingleUnit}

class UnitTransformerTest extends Specification with Matchers {

  import UnitTransformer._

  "UnitTransformer" should {
    "create unit transformer for a minute unit" in {
      val expected = UnitTransformer(60, SingleUnit("minute", Some("min")), SingleUnit("s"))
      createTransformer(SingleUnit("minute")) must beRight(expected)
    }

    "create unit transformer for degree/minute" in {
      val input = ComplexUnit(Div, SingleUnit("degree", Some("d")), SingleUnit("minute", Some("min")))
      val expectedUnit = ComplexUnit(Div, SingleUnit("rad"), SingleUnit("s"))
      val expectedResult = UnitTransformer(Math.PI/(180*60), input, expectedUnit)
      createTransformer(input) must beRight(expectedResult)
    }

    "create unit transformer for degree*minute" in {
      val input = ComplexUnit(Mul, SingleUnit("degree"), SingleUnit("minute"))
      val expectedUnit = ComplexUnit(Mul, SingleUnit("rad"), SingleUnit("s"))
      val expectedResult = UnitTransformer(60*Math.PI/180, input, expectedUnit)
      createTransformer(input) must beRight(expectedResult)
    }

    "create unit transformer for degree*minute*L" in {
      val input = ComplexUnit(Mul, SingleUnit("degree"),
                       ComplexUnit(Mul, SingleUnit("minute"), SingleUnit("L")))

      val expectedUnit = ComplexUnit(Mul, SingleUnit("rad"),
                                ComplexUnit(Mul, SingleUnit("s"), SingleUnit("m\u00B3")))
      val expectedResult = UnitTransformer(60*0.001*Math.PI/180, input, expectedUnit)
      createTransformer(input) must beRight(expectedResult)
    }

    "fail for unknown unit" in {
      val input = SingleUnit("unknown")
      createTransformer(input) must beLeft()
    }
  }
}
