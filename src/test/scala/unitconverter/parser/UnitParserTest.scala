package unitconverter.parser

import org.specs2.matcher.Matchers
import org.specs2.mutable.Specification
import unitconverter.model.CombineOperation.{Div, Mul}
import unitconverter.model.{ComplexUnit, SingleUnit}

class UnitParserTest extends Specification with Matchers {

  val parser = new UnitParser {}

  "UnitParser" should {

    "parse single unit" in {
      parser.parse("rad") must beRight(SingleUnit("rad"))
    }

    "parse (degree/minute)" in {
      parser.parse("(degree/minute)") must beRight(ComplexUnit(Div, SingleUnit("degree"), SingleUnit("minute")))
    }

    "parse (degree*minute)" in {
      parser.parse("(degree*minute)") must beRight(ComplexUnit(Mul, SingleUnit("degree"), SingleUnit("minute")))
    }

    "fail for invalid unit name" in {
      parser.parse("_") must beLeft()
     }
  }
}
