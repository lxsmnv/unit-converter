package unitconverter.parser
import unitconverter.model.{CombineOperation, ComplexUnit, MeasurementUnit, SingleUnit}

import scala.util.parsing.combinator._

/**
 * Parser for expression that combines multiple units. It transforms a string representation of a complex measurement
 * unit to a combination of [[ComplexUnit]] and [[SingleUnit]]
 */
trait UnitParser extends RegexParsers {

  private def unit: Parser[MeasurementUnit]                   = """m\u00B2|m\u00B3|°|'|"|[a-zA-Z]+""".r ^^ { case name => SingleUnit(name) }
  private def mul: Parser[MeasurementUnit => MeasurementUnit] = "*" ~ factor ^^ { case op ~ b => ComplexUnit(CombineOperation.Mul, _, b) }
  private def div: Parser[MeasurementUnit => MeasurementUnit] = "/" ~ factor ^^ { case op ~ b => ComplexUnit(CombineOperation.Div, _, b) }
  private def term: Parser[MeasurementUnit]                   = factor ~ rep(mul | div) ^^ { case a ~ b => b.foldLeft(a)((acc,f) => f(acc)) }
  private def factor: Parser[MeasurementUnit]                 = unit | "(" ~> term <~ ")"

  /**
   * Parse complex unit expression
   * @param line string representation of complex unit
   * @return parsed measurement unit
   */
  final def parse(line: String): Either[String, MeasurementUnit] =
    parseAll(term, line) match {
      case Success(matched, _) => Right(matched)
      case Failure(msg, _)     => Left(msg)
      case Error(msg, _)       => Left(msg)
    }
}
