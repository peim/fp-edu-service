package com.peim.config

import pureconfig.{ConfigReader, ConfigSource}
import pureconfig.generic.semiauto.deriveReader
import zio._

final case class AppConfig(httpConfig: HttpServerConfig, dbConfig: DbConfig)

object AppConfig {

  implicit val configReader: ConfigReader[AppConfig] = deriveReader

  private val source = ConfigSource.default.at("app")

  def layer: ULayer[AppConfig] = ZLayer.fromZIO(
    ZIO
      .attempt(source.loadOrThrow[AppConfig])
      .orDie
  )
}
