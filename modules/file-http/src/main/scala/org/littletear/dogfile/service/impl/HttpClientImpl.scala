package org.littletear.dogfile.service.impl

import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.littletear.dogfile.service.HttpClient
import zio._
import zio.interop.catz._


case class HttpClientImpl() extends HttpClient{
  //此方法不行，需直接外部提供layer
  override def client: IO[Throwable, Client[Task]] = ZIO.scoped(
    ZIO.runtime[Any].flatMap { implicit rts =>
      BlazeClientBuilder[Task].resource.toScopedZIO
    }
  )
}


object HttpClientImpl{
  lazy val live: ZLayer[Any, Nothing, HttpClientImpl] = ZLayer.fromFunction(HttpClientImpl.apply _)
  lazy val clientLive: ZLayer[Any, Throwable, Client[Task]] = ZLayer.scoped(
    ZIO.runtime[Any].flatMap { implicit rts =>
      BlazeClientBuilder[Task].resource.toScopedZIO
    }
  )
}
