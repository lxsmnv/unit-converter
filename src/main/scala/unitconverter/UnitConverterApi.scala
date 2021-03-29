package unitconverter

import io.circe.syntax._
import io.circe._
import io.circe.literal._
import io.circe.generic.auto._
import cats.effect._
import org.http4s._
import org.http4s.dsl.{io, _}
import org.http4s.dsl.io._
import unitconverter.model.{ErrorResult, TransformationResult}
import org.http4s.circe._
import unitconverter.parser.UnitParser

object  UnitConverterApi {


  object UnitsQueryParamMatcher extends QueryParamDecoderMatcher[String]("units")

  def route: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET ->  Root / "si" :? UnitsQueryParamMatcher(units) => {
      processParameter(units)
    }
  }

  private def processParameter(param: String) = {
     val resultOrError = for {
         units <- UnitParser.parse(param)
         tr <- UnitTransformer.createTransformer(units)
       } yield TransformationResult(tr.to.name, tr.factor)

     resultOrError match {
        case Right(tr) =>  Ok(tr.asJson)
        case Left(e)   =>  BadRequest(ErrorResult(e).asJson)
     }
  }

}
