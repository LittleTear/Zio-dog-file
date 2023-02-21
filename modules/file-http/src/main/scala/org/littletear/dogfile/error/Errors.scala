package org.littletear.dogfile.error

object Errors {

  sealed trait Error
  case class ConfigError(e: Throwable)     extends Error
  case class HttpServerError(e: Throwable) extends Error

}