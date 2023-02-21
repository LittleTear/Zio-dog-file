import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.littletear.dogfile.api.EndPointImpl
import org.littletear.dogfile.api.swagger.SwaggerBuilderImpl
import org.littletear.dogfile.route.{ApiRoutes, ApiRoutesImpl}
import org.littletear.dogfile.service.impl.{FileApiServiceImpl, HttpClientImpl, PostRequestImpl}
import org.littletear.dogfile.config.Config
import zio._
import zio.Console.printLine
import zio.logging.LogFormat
import zio.logging.backend.SLF4J
import zio.interop.catz._


object ServerMain extends ZIOAppDefault {
  override val bootstrap = SLF4J.slf4j(LogLevel.Debug, LogFormat.colored)

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    (for {
      _ <- ZIO.logInfo("start server ...")
      apiRoute <- ZIO.service[ApiRoutes]
      config   <- ZIO.service[Config]
      routes   <- apiRoute.routes()
      serverFibre <- ZIO.executor
        .flatMap{excuter =>
          BlazeServerBuilder[Task]
            .withExecutionContext(excuter.asExecutionContext)
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
      _ <- Console.readLine("Press enter to stop the server\n")
      _ <- Console.printLine("Interrupting server")
      _ <- serverFibre.interrupt
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
//    (for{
//    _       <- ZIO.logInfo("start server ...")
//    fileApi <- ZIO.service[FileApiServiceImpl]
//    _       <- fileApi.mainFileServe()
//  } yield ())
//      .provide(
//        FileApiServiceImpl.live,
//        PostRequestImpl.live,
//        HttpClientImpl.live,
//        Config.live,
//        HttpClientImpl.clientLive
//      )

}
