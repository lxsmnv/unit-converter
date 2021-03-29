package unitconverter.model

import unitconverter.model.CombineOperation.{Div, Mul}

sealed trait MeasurementUnit {
  val name: String
  val shortName: Option[String]

  /**
   *
   * @return All known names for the unit
   */
  def names: Seq[String] = Seq(Some(name), shortName).flatten
}

case class SingleUnit(name: String, shortName: Option[String] = None) extends MeasurementUnit

sealed trait CombineOperation
object CombineOperation {
  case object Mul extends CombineOperation
  case object Div extends CombineOperation
}

case class ComplexUnit(op: CombineOperation,
                       left: MeasurementUnit,
                       right: MeasurementUnit
                      ) extends MeasurementUnit {


  val name: String = op match {
    case Mul => s"(${left.name}*${right.name})"
    case Div => s"(${left.name}/${right.name})"
  }

  val shortName: Option[String] = None
}



