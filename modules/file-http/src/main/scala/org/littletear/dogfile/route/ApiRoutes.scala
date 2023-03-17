package org.littletear.dogfile.route
import org.http4s.HttpRoutes
import zio._
import zio.macros.accessible

trait ApiRoutes {
  def routes(): IO[Error, HttpRoutes[Task]]
}

object ApiRoutes{
  def routes(): ZIO[ApiRoutes, Error, HttpRoutes[Task]] = ZIO.serviceWithZIO[ApiRoutes](_.routes())
}
