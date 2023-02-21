package org.littletear.dogfile.domain

import io.circe._
import io.circe.generic.semiauto._

final case class UploadResult(src: String, name: String)

object UploadResult{

  implicit val decoder: Decoder[UploadResult] = deriveDecoder[UploadResult]
  implicit val encoder: Encoder[UploadResult] = deriveEncoder[UploadResult]
}
