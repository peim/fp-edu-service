package com.peim.config

import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

final case class DbConfig(driver: String, url: String, user: String, password: String, poolName: String, maxConnections: Int, connectionTimeout: Long)

object DbConfig {
  implicit val configReader: ConfigReader[DbConfig] = deriveReader
}
