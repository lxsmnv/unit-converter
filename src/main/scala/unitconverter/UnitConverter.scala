package unitconverter

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

/**
 * Main application class
 */
object UnitConverter extends IOApp {

  implicit val es = java.util.concurrent.ForkJoinPool.commonPool
  implicit val forkJoinPool = scala.concurrent.ExecutionContext.fromExecutor(es)

  override def run(args: List[String]): IO[ExitCode] =
    // Create and run Http4s server
    BlazeServerBuilder[IO](forkJoinPool)
      .bindHttp()
      .withNio2(true)
      .withHttpApp(createRoutes())
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

  private def createRoutes() =
    // Http routing
    Logger.httpApp[IO](logHeaders = true, logBody = false)(
         Router(
           "/units" -> UnitConverterApi.route
         ).orNotFound
    )
}
