package com.peim.config

import cats.effect._
import cats.implicits._
import doobie._
import doobie.hikari._

import scala.concurrent.ExecutionContext

object Database {

  implicit val cs = IO.contextShift(ExecutionContext.global)

  val transactor: Resource[IO, HikariTransactor[IO]] =
    for {
      connectEC  <- ExecutionContexts.fixedThreadPool[IO](8)
      transactEC <- ExecutionContexts.cachedThreadPool[IO]
    } yield HikariTransactor(DbConfig.dataSource, connectEC, transactEC)

}
