package unitconverter

import cats.effect.IO
import org.http4s.{HttpVersion, Method, Request, Uri}
import org.http4s.circe.jsonOf
import org.specs2.mutable.Specification
import org.specs2.matcher.IOMatchers
import unitconverter.model.TransformationResult


class UnitConverterApiTest extends Specification with IOMatchers {


  implicit val resultDecoder = jsonOf[IO, TransformationResult]
/*
  "UnitConverterApi" should {
    "process valid unit" in {
      val req = Request[IO](Method.GET, Uri.unsafeFromString("/units/si?units=rad"), HttpVersion.`HTTP/1.1`)
      def result = UnitConverterApi.route
        .run(req)
        .map(resp => resp.as[TransformationResult].map(tr => tr))

      result must returnValue((200, TransformationResult("rad", 1.0)))
    }

  }
*/
}
