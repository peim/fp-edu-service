package com.peim.config

import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

final case class HttpServerConfig(host: String, port: Int)

object HttpServerConfig {
  implicit val configReader: ConfigReader[HttpServerConfig] = deriveReader
}