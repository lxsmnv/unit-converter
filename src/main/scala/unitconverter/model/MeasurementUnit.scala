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

  /**
   * Other name that can be used for this unit e.g. short form
   */
  val alias: Option[String]

  /**
   *
   * @return All known names for the unit
   */
  def names: Seq[String] = Seq(Some(name), alias).flatten
}

/**
 * Single unit
 *
 * @param name unit name
 * @param alias unit alias if exists
 */
case class SingleUnit(name: String, alias: Option[String] = None) extends MeasurementUnit

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



