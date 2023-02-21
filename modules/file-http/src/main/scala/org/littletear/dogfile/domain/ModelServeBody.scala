package org.littletear.dogfile.domain

import io.circe._
import io.circe.generic.semiauto._

case class ModelServeBody(
                         text: String,
                         env: Env
                         )


object ModelServeBody{
  implicit val decoder: Decoder[ModelServeBody] = deriveDecoder[ModelServeBody]
  implicit val encoder: Encoder[ModelServeBody] = deriveEncoder[ModelServeBody]
}