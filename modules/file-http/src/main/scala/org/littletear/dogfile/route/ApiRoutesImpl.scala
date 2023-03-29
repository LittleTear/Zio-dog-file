package org.littletear.dogfile.route

import org.littletear.dogfile.api.ComEndPoint
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio._
import org.http4s.HttpRoutes
import org.littletear.dogfile.api.swagger.SwaggerBuilder
import zio.http._
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

  override def zioRoutes(): IO[Error,  HttpApp[Any, Throwable]] = {
    for{
      uploadFile <- endPoint.uploadFileEndPoint
      health     <- endPoint.checkHealthEndPoint
      analyze    <- endPoint.analyzeEndPoint
      routes     = List(uploadFile, health, analyze)
//      swagger    <- swaggerBuilder.build(routes.map(_.endpoint))
      api        =  ZioHttpInterpreter().toHttp(routes)
    } yield api
  }
}

object ApiRoutesImpl{
  lazy val live: ZLayer[ComEndPoint with SwaggerBuilder, Nothing, ApiRoutesImpl] = ZLayer.fromFunction(ApiRoutesImpl.apply _)
}
