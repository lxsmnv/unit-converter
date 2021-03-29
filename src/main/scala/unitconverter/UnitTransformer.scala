package unitconverter

import unitconverter.model.CombineOperation.{Div, Mul}
import unitconverter.model.{ComplexUnit, MeasurementUnit, SingleUnit}

/**
 * Unit transformation
 * @param factor transformation factor
 * @param from source unit
 * @param to target unit
 */
case class UnitTransformer(factor: Double,
                           from: MeasurementUnit,
                           to: MeasurementUnit
                           )

object UnitTransformer {

  // Base transformations
  private val unitTransformers = Seq(
    UnitTransformer( 60.0, SingleUnit("minute", Some("min")), SingleUnit("s")),
    UnitTransformer(3600.0, SingleUnit("hour", Some("h")), SingleUnit("s")),
    UnitTransformer(86400.0, SingleUnit("day", Some("d")), SingleUnit("s")),
    UnitTransformer(Math.PI/180, SingleUnit("degree", Some("°")), SingleUnit("rad")),
    UnitTransformer(Math.PI/10800, SingleUnit("arcminute", Some("'")), SingleUnit("rad")),
    UnitTransformer(Math.PI/648000, SingleUnit("arcsecond", Some("\"")), SingleUnit("rad")),
    UnitTransformer(10000, SingleUnit("hectare",Some("ha")), SingleUnit("m\u00B2")),
    UnitTransformer(0.001, SingleUnit("litre", Some("L")), SingleUnit("m\u00B3")),
    UnitTransformer(1000, SingleUnit("tonne", Some("t")), SingleUnit("kg"))
  )

  // Transformation mapping "unit name" -> UnitTransformer
  private val conversionTable: Map[String, UnitTransformer] =
    (
    unitTransformers
      .flatMap(t => t.from.names.map(name => name -> t))
      .toMap
    ++
    // SI to SI unit transformation e.g. rad to rad
    unitTransformers
      .flatMap(t => t.to.names.map(name => name -> UnitTransformer(1.0, t.to, t.to)))
      .toMap
    )

  /**
   * This function will create transformer for complex unit
   *
   * @param unit
   * @return [[UnitTransformer]]
   */
  def createTransformer(unit: MeasurementUnit): Either[String, UnitTransformer] =
    unit match {
      case SingleUnit(name, _) => conversionTable.get(name).toRight[String](s"Unit $name not found")
      case cu @ ComplexUnit(op, left, right)  =>
            for {
              l <- createTransformer(left)
              r <- createTransformer(right)
              f = op match {
                case Mul => l.factor * r.factor
                case Div => l.factor / r.factor
              }
            } yield UnitTransformer(f, cu, ComplexUnit(op, l.to, r.to))
    }
}


