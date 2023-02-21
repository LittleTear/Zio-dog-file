package org.littletear.dogfile.config

import org.littletear.dogfile.error.Errors.ConfigError
import zio.config._
import zio.config.magnolia.descriptor
import zio.config.typesafe.TypesafeConfigSource
import zio._

case class Config(
                   url_file_path:UrlPathConfig,
                   api:ApiConfig
                 )

case class UrlPathConfig(
                          mode_serve_url:String,
                          tao_ge_url: String,
                          long_ge_url:String,
                          input_data_path: String,
                          output_data_path: String
                    )

case class ApiConfig (
                       host: String = "localhost",
                       port: Int = 8080
                     )

object Config{
  lazy val live: ZLayer[Any, ConfigError, Config] = ZLayer{
    read{
      descriptor[Config].from(
        TypesafeConfigSource.fromResourcePath
      )
    }
  }.mapError(e => ConfigError(e))
}