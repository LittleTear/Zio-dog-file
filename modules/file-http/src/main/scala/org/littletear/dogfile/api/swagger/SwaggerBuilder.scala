package org.littletear.dogfile.api.swagger

import org.littletear.dogfile.config.Config
import sttp.tapir.AnyEndpoint
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.SwaggerUIOptions
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import zio.macros.accessible
import zio.{BuildInfo, IO, Task, URLayer, ZIO, ZLayer}

@accessible
trait SwaggerBuilder {
  def build(endpoints: List[AnyEndpoint]): IO[Error, List[ServerEndpoint[Any, Task]]]
}

case class SwaggerBuilderImpl(config: Config) extends SwaggerBuilder {
  override def build(endpoints: List[AnyEndpoint]): IO[Error, List[ServerEndpoint[Any, Task]]] =
    ZIO.succeed(
      SwaggerInterpreter(
        swaggerUIOptions = SwaggerUIOptions(
          pathPrefix = List("swagger"),
          yamlName = "api.yaml",
          contextPath = Nil,
          useRelativePaths = true
        )
      ).fromEndpoints[Task](
        endpoints = endpoints,
        title = "KTAE analyzer API",
        version = BuildInfo.version
      )
    )
}

object SwaggerBuilderImpl {
  val live: URLayer[Config, SwaggerBuilder] = ZLayer.fromFunction(SwaggerBuilderImpl(_))
}
