package unitconverter

import io.circe.syntax._
import io.circe.generic.auto._
import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import unitconverter.model.{ErrorResult, TransformationResult}
import org.http4s.circe._
import unitconverter.parser.UnitParser

/**
 * HTTP end point processing for unit conversion.
 */
object  UnitConverterApi extends UnitParser {


  private object UnitsQueryParamMatcher extends QueryParamDecoderMatcher[String]("units")

  def route: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET ->  Root / "si" :? UnitsQueryParamMatcher(units) => {
      convertUnit(units)
    }
  }

  /**
   * Process unit conversion request
   *
   * @param unitExpr string representation of one or multiple units
   * @return Response with converted measurement unit
   */
  private def convertUnit(unitExpr: String): IO[Response[IO]] = {
     val resultOrError = for {
         units <- parse(unitExpr)
            tr <- UnitTransformation.createTransformer(units)
       } yield TransformationResult(tr.unit.name, tr.factor)

     resultOrError match {
        case Right(tr) =>  Ok(tr.asJson)
        case Left(e)   =>  BadRequest(ErrorResult(e).asJson)
     }
  }

}
