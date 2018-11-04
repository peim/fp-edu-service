package com.peim.config

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DbConfig {

  private val config = ConfigFactory.load()

  private val driver: String          = config.getString("default.db.driver")
  private val url: String             = config.getString("default.db.url")
  private val user: String            = config.getString("default.db.user")
  private val password: String        = config.getString("default.db.password")
  private val poolName: String        = config.getString("default.db.poolName")
  private val maxConnections: Int     = config.getInt("default.db.maxConnections")
  private val connectionTimeout: Long = config.getLong("default.db.connectionTimeout")

  val dataSource: HikariDataSource = {
    val hikariConfig = new HikariConfig

    hikariConfig.setPoolName(poolName)
    hikariConfig.setDriverClassName(driver)
    hikariConfig.setJdbcUrl(url)
    hikariConfig.setUsername(user)
    hikariConfig.setPassword(password)
    hikariConfig.setMaximumPoolSize(maxConnections)
    hikariConfig.setConnectionTimeout(connectionTimeout)

    new HikariDataSource(hikariConfig)
  }

}
