package unitconverter

import unitconverter.model.CombineOperation.{Div, Mul}
import unitconverter.model.{ComplexUnit, MeasurementUnit, SingleUnit}

/**
 * Unit transformation
 * @param factor transformation factor
 * @param unit target unit
 */
case class UnitTransformation(factor: Double, unit: MeasurementUnit)

object UnitTransformation {

  // Base transformations
  private val unitTransformers = Seq(
    Seq("min","minute") -> UnitTransformation( 60.0, SingleUnit("s")),
    Seq("h", "hour") -> UnitTransformation(3600.0, SingleUnit("s")),
    Seq("d", "day") -> UnitTransformation(86400.0, SingleUnit("s")),
    Seq("°", "degree") -> UnitTransformation(Math.PI/180, SingleUnit("rad")),
    Seq("'", "arcminute") -> UnitTransformation(Math.PI/10800, SingleUnit("rad")),
    Seq("\"", "arcsecond") -> UnitTransformation(Math.PI/648000, SingleUnit("rad")),
    Seq("ha", "hectare") -> UnitTransformation(10000, SingleUnit("m\u00B2")),
    Seq("L", "litre") -> UnitTransformation(0.001, SingleUnit("m\u00B3")),
    Seq("t", "tonne") -> UnitTransformation(1000, SingleUnit("kg"))
  )

  // Transformation mapping "unit name" -> UnitTransformer
  private val conversionTable: Map[String, UnitTransformation] =
    (
    unitTransformers
      .flatMap { case (names, tr) => names.map(n => n->tr) }
      .toMap
    ++
    // SI to SI unit transformation e.g. rad to rad
    unitTransformers
      .map { case (_, tr) => tr.unit }
      .map(unit => unit.name -> UnitTransformation(1.0, unit))
      .toMap
    )

  /**
   * This function will create transformer for complex unit
   *
   * @param unit
   * @return [[UnitTransformation]]
   */
  def createTransformer(unit: MeasurementUnit): Either[String, UnitTransformation] =
    unit match {
      case SingleUnit(name) => conversionTable.get(name).toRight[String](s"Unit $name not found")
      case ComplexUnit(op, left, right)  =>
            for {
              l <- createTransformer(left)
              r <- createTransformer(right)
              f = op match {
                case Mul => l.factor * r.factor
                case Div => l.factor / r.factor
              }
            } yield UnitTransformation(f, ComplexUnit(op, l.unit, r.unit))
    }
}


