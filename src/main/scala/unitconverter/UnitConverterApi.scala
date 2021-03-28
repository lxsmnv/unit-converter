package unitconverter

import cats.effect.IO
import org.http4s._
import org.http4s.dsl._
import org.http4s.dsl.io._
import unitconverter.model.TransformationResult
import org.http4s.circe._
import io.circe.syntax._
import io.circe._
import io.circe.literal._

import unitconverter.parser.UnitParser

object  UnitConverterApi {

  object UnitsQueryParamMatcher extends QueryParamDecoderMatcher[String]("units")

  def route: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET ->  Root / "si" :? UnitsQueryParamMatcher(units) => {
      Ok("Service is online.")
    }
  }


  private def processParameter(param: String) = {
     val units = UnitParser.parse(param)
     val resOrError =
       UnitTransformer
       .createTransformer(units)
       .map(tr => TransformationResult("", tr.factor))

    resOrError match {
      case Right(tr) =>  Ok(tr.asJson)
      case Left(e)   =>  BadRequest(e)
    }


  }

}
