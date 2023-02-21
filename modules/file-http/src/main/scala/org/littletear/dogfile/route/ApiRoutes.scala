package org.littletear.dogfile.route
import org.http4s.HttpRoutes
import zio._
import zio.macros.accessible

@accessible
trait ApiRoutes {
  def routes(): IO[Error, HttpRoutes[Task]]
}
