package unitconverter

import cats.effect.{ExitCode, IO, IOApp}
import io.circe.generic.extras.Configuration
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import unitconverter.parser.UnitParser

object UnitConverter extends IOApp {

  implicit val es = java.util.concurrent.ForkJoinPool.commonPool
  implicit val forkJoinPool = scala.concurrent.ExecutionContext.fromExecutor(es)

  override def run(args: List[String]): IO[ExitCode] = {
    startServer()
  }

  private def startServer() =
    BlazeServerBuilder[IO](forkJoinPool)
      .bindHttp()
      .withNio2(true)
      .withHttpApp(createRoutes())
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

  private def createRoutes() =
    Logger.httpApp[IO](logHeaders = true, logBody = false)(
         Router(
           "/units" -> UnitConverterApi.route
         ).orNotFound
    )
}
