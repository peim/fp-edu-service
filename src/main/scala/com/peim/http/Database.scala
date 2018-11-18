package com.peim.http

import cats.effect._
import cats.implicits._
import com.peim.config.DbConfig
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor

object Database {

  def createTransactor[F[_]: ContextShift](dbConfig: DbConfig)(
      implicit cs: Async[F]): Resource[F, HikariTransactor[F]] = {
    for {
      connectEC  <- ExecutionContexts.fixedThreadPool[F](32)
      transactEC <- ExecutionContexts.cachedThreadPool[F]
    } yield HikariTransactor(dbConfig.dataSource, connectEC, transactEC)
  }

}
