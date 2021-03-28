package unitconverter.model

sealed trait MeasurementUnit

case class SingleUnit(name: String) extends MeasurementUnit

sealed trait CombineOperation

object CombineOperation {
  case object Mul extends CombineOperation
  case object Div extends CombineOperation
}

case class ComplexUnit(op: CombineOperation,
                       left: MeasurementUnit,
                       right: MeasurementUnit
                      ) extends MeasurementUnit



