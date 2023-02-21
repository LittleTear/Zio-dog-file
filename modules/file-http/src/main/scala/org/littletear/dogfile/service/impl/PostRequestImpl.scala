package org.littletear.dogfile.service.impl

import io.circe._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.{Headers, MediaType, Method, Request, Uri}
import org.littletear.dogfile.service.{HttpClient, PostRequest}
import zio._
import zio.interop.catz._
import org.http4s.Method._
import org.http4s.headers._

case class PostRequestImpl(httpClient:Client[Task]) extends PostRequest{
  override def post(url: String, body: Json): Task[String] = {
    for{
      _   <- ZIO.logInfo("start client ")
      req = Request[Task](Method.POST, Uri.unsafeFromString(url), headers = Headers(Accept(MediaType.application.json))).withEntity(body)
      result <- httpClient.expect[String](req)
    } yield result
  }

  override def post(url: String, params:Map[String,String]): Task[String] = {
    for {
      _   <- ZIO.logInfo("start client ")
      req = Request[Task](Method.POST, Uri.unsafeFromString(url).withQueryParams(params),headers = Headers(Accept(MediaType.application.json))).withEmptyBody
      result <- httpClient.expect[String](req)
    } yield result
  }
}

object PostRequestImpl{
  lazy val live: ZLayer[Client[Task], Nothing, PostRequestImpl] = ZLayer.fromFunction(PostRequestImpl.apply _)
}
