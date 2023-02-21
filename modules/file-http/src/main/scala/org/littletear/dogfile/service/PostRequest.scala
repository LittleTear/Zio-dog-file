package org.littletear.dogfile.service

import io.circe.Json
import zio.Task

trait PostRequest {

  def post(url: String, body: Json): Task[String]
  def post(url: String, params: Map[String,String]): Task[String]


}
