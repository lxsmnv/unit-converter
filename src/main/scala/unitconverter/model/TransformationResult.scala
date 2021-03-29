package unitconverter.model

import io.circe.generic.extras._

/**
 * Transformation result
 * @param unitName text representation of unit name
 * @param multiplicationFactor multiplication factor
 */
@ConfiguredJsonCodec
case class TransformationResult(unitName: String, multiplicationFactor: Double)

object TransformationResult {
  implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames
}


