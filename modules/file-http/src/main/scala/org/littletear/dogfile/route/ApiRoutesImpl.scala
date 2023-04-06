package org.littletear.dogfile.route

import org.littletear.dogfile.api.ComEndPoint
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import zio._
import org.http4s.HttpRoutes
import org.littletear.dogfile.api.swagger.SwaggerBuilder
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
case class ApiRoutesImpl(endPoint:ComEndPoint,swaggerBuilder: SwaggerBuilder) extends ApiRoutes {

  override def routes(): IO[Error, HttpRoutes[Task]] =
    for{
      uploadFile    <- endPoint.uploadFileEndPoint
      health        <- endPoint.checkHealthEndPoint
      analyze       <- endPoint.analyzeEndPoint
      checkComplete <- endPoint.checkFileIsAnalyzeComplete
      download      <- endPoint.downloadFile
      routes        =  List(uploadFile,health,analyze,checkComplete,download)
      swagger       <- swaggerBuilder.build(routes.map(_.endpoint))
      api           = ZHttp4sServerInterpreter().from(routes ++ swagger).toRoutes
    } yield api

  override def streamRoutes: ZIO[Any, Nothing, HttpRoutes[Task]]=
    for {
      download      <- endPoint.downloadFile
      streamApi = ZHttp4sServerInterpreter().from(download).toRoutes
    } yield streamApi
}

object ApiRoutesImpl{
  lazy val live: ZLayer[ComEndPoint with SwaggerBuilder, Nothing, ApiRoutesImpl] = ZLayer.fromFunction(ApiRoutesImpl.apply _)
}
