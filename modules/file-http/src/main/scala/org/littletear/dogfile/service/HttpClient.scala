package org.littletear.dogfile.service
import org.http4s.client.Client
import zio._
import zio.macros.accessible

@accessible
trait HttpClient {
  def client: IO[Throwable, Client[Task]]
}
