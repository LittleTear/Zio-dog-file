package org.littletear.dogfile.domain
import io.circe._
import io.circe.generic.semiauto._

case class Animal(name:String) extends Serializable

object Animal{
  implicit val decoder: Decoder[Animal] = deriveDecoder[Animal]
  implicit val encoder: Encoder[Animal] = deriveEncoder[Animal]
}