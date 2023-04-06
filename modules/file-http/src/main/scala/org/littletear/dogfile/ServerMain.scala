package org.littletear.dogfile
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.littletear.dogfile.api.EndPointImpl
import org.littletear.dogfile.api.swagger.SwaggerBuilderImpl
import org.littletear.dogfile.route.{ApiRoutes, ApiRoutesImpl}
import org.littletear.dogfile.service.impl.{FileApiServiceImpl, HttpClientImpl, PostRequestImpl}
import org.littletear.dogfile.config.Config
import zio._
import zio.logging.LogFormat
import zio.logging.backend.SLF4J
import zio.interop.catz._


object ServerMain extends ZIOAppDefault {
  override val bootstrap = SLF4J.slf4j(LogLevel.Debug, LogFormat.colored)
  private val slf4jLogger = org.slf4j.LoggerFactory.getLogger("SLF4J-LOGGER")

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    (for {
      _ <- ZIO.logInfo("start server ...")
      config   <- ZIO.service[Config]
      routes   <- ApiRoutes.routes()
      serverFibre <- ZIO.executor
        .flatMap{executer =>
          BlazeServerBuilder[Task]
            .withExecutionContext(executer.asExecutionContext)
            .bindHttp(config.api.port,config.api.host)
            .withHttpApp(
              Router[Task](
                "" -> routes
              ).orNotFound
            )
            .serve
            .compile
            .drain
        }.fork
//      _ <- Console.readLine("Press enter to stop the server\n")
//      _ <- Console.printLine("Interrupting server")
      _ <- serverFibre.join
    } yield ())
      .provide(
        EndPointImpl.live,
        ApiRoutesImpl.live,
        FileApiServiceImpl.live,
        PostRequestImpl.live,
        HttpClientImpl.live,
        Config.live,
        HttpClientImpl.clientLive,
        SwaggerBuilderImpl.live
      )
}
