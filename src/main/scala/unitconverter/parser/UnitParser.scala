package unitconverter.parser
import unitconverter.model.{CombineOperation, ComplexUnit, MeasurementUnit, SingleUnit}

import scala.util.parsing.combinator._

object UnitParser extends RegexParsers {
  private def unit: Parser[MeasurementUnit]                   = """m\u00B2|m\u00B3|°|'|"|[a-zA-Z]+""".r ^^ { case name => SingleUnit(name) }
  private def mul: Parser[MeasurementUnit => MeasurementUnit] = "*" ~ factor ^^ { case op ~ b => ComplexUnit(CombineOperation.Mul, _, b) }
  private def div: Parser[MeasurementUnit => MeasurementUnit] = "/" ~ factor ^^ { case op ~ b => ComplexUnit(CombineOperation.Div, _, b) }
  private def term: Parser[MeasurementUnit]                   = factor ~ rep(mul | div) ^^ { case a ~ b => b.foldLeft(a)((acc,f) => f(acc)) }
  private def factor: Parser[MeasurementUnit]                 = unit | "(" ~> term <~ ")"

  def parse(line: String): Either[String, MeasurementUnit] =
    parseAll(term, line) match {
      case Success(matched, _) => Right(matched)
      case Failure(msg, _)     => Left(msg)
      case Error(msg, _)       => Left(msg)
    }
}
