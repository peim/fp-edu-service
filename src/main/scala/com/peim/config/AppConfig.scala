package com.peim.config

import com.typesafe.config.ConfigFactory

class AppConfig {

  private val config = ConfigFactory.load()

  val systemName: String = "fp-edu-service"

  val httpHost: String = config.getString("http.host")
  val httpPort: Int    = config.getInt("http.port")

  val dbConfig: DbConfig = new DbConfig(config)

}
