package com.peim.config

import cats.effect._
import cats.implicits._
import doobie._
import doobie.hikari._

import scala.concurrent.ExecutionContext

object Database {

  def transactor(implicit ec: ExecutionContext): Resource[IO, HikariTransactor[IO]] = {
    implicit val cs: ContextShift[IO] = IO.contextShift(ec)
    for {
      connectEC  <- ExecutionContexts.fixedThreadPool[IO](8)
      transactEC <- ExecutionContexts.cachedThreadPool[IO]
    } yield HikariTransactor(DbConfig.dataSource, connectEC, transactEC)
  }

}
