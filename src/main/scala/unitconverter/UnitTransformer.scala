package unitconverter

import unitconverter.model.CombineOperation.{Div, Mul}
import unitconverter.model.{ComplexUnit, MeasurementUnit, SingleUnit}

sealed trait UnitTransformer {
  val name: String
  val factor: Double
  val from: MeasurementUnit
  val to: MeasurementUnit
}

case class SimpleUnitTransformer(override val name: String,
                                 override val factor: Double,
                                 override val from: MeasurementUnit,
                                 override val to: MeasurementUnit
                                ) extends UnitTransformer


object UnitTransformer {
  private val conversionTable = Map[String, UnitTransformer] (
    "minute" -> SimpleUnitTransformer("s", 60.0, SingleUnit("minute"), SingleUnit("s")),
    "hour" -> SimpleUnitTransformer("s", 3600.0, SingleUnit("hour"), SingleUnit("s")),
    "day" -> SimpleUnitTransformer("s", 86400.0, SingleUnit("day"), SingleUnit("s")),
    "degree" -> SimpleUnitTransformer("s", Math.PI/180, SingleUnit("degree"), SingleUnit("rad")),
    "arcminute" -> SimpleUnitTransformer("s", Math.PI/10800, SingleUnit("arcminute"), SingleUnit("rad")),
    "arcsecond" -> SimpleUnitTransformer("s", Math.PI/648000, SingleUnit("arcsecond"), SingleUnit("rad")),
    "hectare" ->  SimpleUnitTransformer("s", 10000, SingleUnit("hectare"), SingleUnit("m2")),
    "litre" -> SimpleUnitTransformer("s", 0.001, SingleUnit("litre"), SingleUnit("m3")),
    "tonne" -> SimpleUnitTransformer("s", 1000, SingleUnit("tonne"), SingleUnit("kg"))
  )


  def createTransformer(unit: MeasurementUnit): Either[String, UnitTransformer] = {
    unit match {
      case SingleUnit(name) => conversionTable.get(name).toRight[String](s"Unit $name not found")
      case cu @ ComplexUnit(op, left, right)  =>  {
            val leftTr = createTransformer(left)
            val rightTr = createTransformer(right)
            for {
              l <- leftTr
              r <- rightTr
              f = op match {
                case Mul => l.factor * r.factor
                case Div => l.factor / r.factor
              }
            } yield SimpleUnitTransformer("", f, cu, ComplexUnit(op, l.to, r.to))
      }
    }
  }

}


