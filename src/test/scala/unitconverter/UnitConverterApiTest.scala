package unitconverter

import cats.effect.IO
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.specs2.mutable.Specification
import org.specs2.matcher.{Http4sMatchers, IOMatchers}
import unitconverter.model.TransformationResult


class UnitConverterApiTest extends Specification with IOMatchers with Http4sMatchers[IO] {


  implicit val resultDecoder = jsonOf[IO, TransformationResult]
  val service = UnitConverterApi.route.orNotFound

  "UnitConverterApi" should {
    "process valid unit" in {
      val req = Request[IO](Method.GET, Uri.unsafeFromString("/si?units=rad"), HttpVersion.`HTTP/1.1`)
      val result = service.run(req)

      result must returnValue { (response: Response[IO]) =>
        response must haveStatus(Ok)
        response must haveBody(TransformationResult("rad", 1.0))
      }
    }

   "fail processing invalid unit" in {
     val req = Request[IO](Method.GET, Uri.unsafeFromString("/si?units=invalid"), HttpVersion.`HTTP/1.1`)
     val result = service.run(req)

     result must returnValue { (response: Response[IO]) =>
       response must haveStatus(BadRequest)
     }
   }

    "process (degree/minute)" in {
      val req = Request[IO](Method.GET, Uri.unsafeFromString("/si?units=(degree/minute)"), HttpVersion.`HTTP/1.1`)
      val result = service.run(req)

      result must returnValue { (response: Response[IO]) =>
        response must haveStatus(Ok)
        response must haveBody(TransformationResult("(rad/s)", Math.PI/(180*60)))
      }
    }

    "process (degree/(minute*hectare))" in {
      val req = Request[IO](Method.GET, Uri.unsafeFromString("/si?units=(degree/(minute*hectare))"), HttpVersion.`HTTP/1.1`)
      val result = service.run(req)

      result must returnValue { (response: Response[IO]) =>
        response must haveStatus(Ok)
        response must haveBody(TransformationResult("(rad/(s*m²))", Math.PI/(180*60*10000)))
      }
    }

    "process (ha*%C2%B0)" in {
      val req = Request[IO](Method.GET, Uri.unsafeFromString("/si?units=(ha*%C2%B0)"), HttpVersion.`HTTP/1.1`)
      val result = service.run(req)

      result must returnValue { (response: Response[IO]) =>
        response must haveStatus(Ok)
        response must haveBody(TransformationResult("(m²*rad)", 10000*Math.PI/180))
      }
    }

    "process all units by name" in {
      val req = Request[IO](Method.GET,
        Uri.unsafeFromString("/si?units=minute*hour*day*degree*arcminute*arcsecond*hectare*litre*tonne"),
        HttpVersion.`HTTP/1.1`)

      val result = service.run(req)

      result must returnValue { (response: Response[IO]) =>
        response must haveStatus(Ok)
      }
    }

    "process all units by symbol" in {
      val req = Request[IO](Method.GET, Uri.unsafeFromString("/si?units=min*h*d*%C2%B0*%27*%22*ha*L*t"), HttpVersion.`HTTP/1.1`)
      val result = service.run(req)

      result must returnValue { (response: Response[IO]) =>
        response must haveStatus(Ok)
      }
    }

    "process all SI units" in {
      val req = Request[IO](Method.GET, Uri.unsafeFromString("/si?units=s*rad*m%C2%B2*m%C2%B3*kg"), HttpVersion.`HTTP/1.1`)
      val result = service.run(req)

      result must returnValue { (response: Response[IO]) =>
        response must haveStatus(Ok)
      }
    }
  }

}
