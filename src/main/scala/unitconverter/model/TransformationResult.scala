package unitconverter.model

import io.circe.generic.extras._

@ConfiguredJsonCodec
case class TransformationResult(unitName: String, multiplicationFactor: Double)

object TransformationResult {
  implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames
}


