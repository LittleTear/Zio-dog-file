package org.littletear.dogfile.domain

import io.circe._
import io.circe.generic.semiauto._

case class Env(
                requestId:String,
                model: String,
                vin: String,
                sessionId: String
              )

object Env{
  implicit val decoder: Decoder[Env] = deriveDecoder[Env]
  implicit val encoder: Encoder[Env] = deriveEncoder[Env]
}
