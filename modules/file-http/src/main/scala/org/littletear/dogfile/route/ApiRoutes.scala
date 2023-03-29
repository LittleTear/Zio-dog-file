package org.littletear.dogfile.route
import org.http4s.HttpRoutes
import zio._
import zio.http.HttpApp
import zio.macros.accessible

trait ApiRoutes {
  def routes(): IO[Error, HttpRoutes[Task]]

  def zioRoutes(): IO[Error,  HttpApp[Any, Throwable]]
}

object ApiRoutes{
  def routes(): ZIO[ApiRoutes, Error, HttpRoutes[Task]] = ZIO.serviceWithZIO[ApiRoutes](_.routes())

  def zioRoutes(): ZIO[ApiRoutes, Error,  HttpApp[Any, Throwable]] = ZIO.serviceWithZIO[ApiRoutes](_.zioRoutes())
}
