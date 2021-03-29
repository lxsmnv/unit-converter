package unitconverter.model

import unitconverter.model.CombineOperation.{Div, Mul}

// ADT for measurement units

/**
 * Abstract measurement unit
 */
sealed trait MeasurementUnit {
  /**
   * Measurement unit name
   */
  val name: String
}

/**
 * Single unit
 *
 * @param name unit name
 */
case class SingleUnit(name: String) extends MeasurementUnit

/**
 * Operations used to combine units
 */
sealed trait CombineOperation
object CombineOperation {
  // Unit multiplication operation
  case object Mul extends CombineOperation
  // Unit division operation
  case object Div extends CombineOperation
}

/**
 * Complex unit that combines two measurement units
 *
 * @param op operation that is used to combine units (* or /)
 * @param left left argument of the combination operation
 * @param right right argument of the combination operation
 */
case class ComplexUnit(op: CombineOperation,
                       left: MeasurementUnit,
                       right: MeasurementUnit
                      ) extends MeasurementUnit {


  val name: String = op match {
    case Mul => s"(${left.name}*${right.name})"
    case Div => s"(${left.name}/${right.name})"
  }

  val alias: Option[String] = None
}



