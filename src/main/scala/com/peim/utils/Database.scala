package com.peim.utils

import cats.effect.{Async, ContextShift, Resource}
import com.peim.config.DbConfig
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

object Database {

  def createTransactor[F[_]: ContextShift](dbConfig: DbConfig)(
      implicit cs: Async[F]): Resource[F, HikariTransactor[F]] = {
    for {
      connectEC  <- ExecutionContexts.fixedThreadPool[F](32)
      transactEC <- ExecutionContexts.cachedThreadPool[F]
    } yield HikariTransactor(dbConfig.dataSource, connectEC, transactEC)
  }

}
