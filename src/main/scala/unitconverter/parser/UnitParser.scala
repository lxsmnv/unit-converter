package unitconverter.parser
import unitconverter.model.CombineOperation.Mul
import unitconverter.model.{CombineOperation, ComplexUnit, MeasurementUnit, SingleUnit}

import scala.util.parsing.combinator._

object UnitParser extends RegexParsers {
  def unit: Parser[MeasurementUnit]   = """\xF8|'|"|[a-zA-Z]+""".r ^^ { case name => SingleUnit(name) }
  def mul: Parser[MeasurementUnit => MeasurementUnit]    = "*" ~ factor ^^ { case "*" ~ b => ComplexUnit(CombineOperation.Mul, _, b) }
  def div: Parser[MeasurementUnit => MeasurementUnit]    = "/" ~ factor ^^ { case "/" ~ b => ComplexUnit(CombineOperation.Div, _, b) }
  def term:   Parser[MeasurementUnit]    = factor ~ rep(mul | div) ^^ { case a ~ b => b.foldLeft(a)((acc,f) => f(acc)) }
  def factor: Parser[MeasurementUnit]    = unit | "(" ~> term <~ ")"

  def parse(line: String) = parseAll(term, line).get
}
