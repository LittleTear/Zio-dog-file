package org.littletear.dogfile.route

import org.littletear.dogfile.api.{ComEndPoint}
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import zio._
import org.http4s.HttpRoutes
import org.littletear.dogfile.api.swagger.SwaggerBuilder
case class ApiRoutesImpl(endPoint:ComEndPoint,swaggerBuilder: SwaggerBuilder) extends ApiRoutes {

  override def routes(): IO[Error, HttpRoutes[Task]] =
    for{
      uploadFile <- endPoint.uploadFileEndPoint
      health     <- endPoint.checkHealthEndPoint
      analyze    <- endPoint.analyzeEndPoint
      routes     =  List(uploadFile,health,analyze)
      swagger    <- swaggerBuilder.build(routes.map(_.endpoint))
      api        = ZHttp4sServerInterpreter().from(routes ++ swagger).toRoutes
    } yield api
}

object ApiRoutesImpl{
  lazy val live: ZLayer[ComEndPoint with SwaggerBuilder, Nothing, ApiRoutesImpl] = ZLayer.fromFunction(ApiRoutesImpl.apply _)
}
