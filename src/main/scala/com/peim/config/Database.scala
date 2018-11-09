package com.peim.config

import cats.effect._
import cats.implicits._
import doobie._
import doobie.hikari._
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.ExecutionContext

object Database {

  def transactor(implicit s: Scheduler): Resource[Task, HikariTransactor[Task]] = {
    implicit val contextShift: ContextShift[Task] =
      new ContextShift[Task] {
        override def shift: Task[Unit] =
          Task.shift(s)
        override def evalOn[A](ec: ExecutionContext)(fa: Task[A]): Task[A] =
          Task.shift(ec).bracket(_ => fa)(_ => Task.shift(s))
      }

    for {
      connectEC  <- ExecutionContexts.fixedThreadPool[Task](8)
      transactEC <- ExecutionContexts.cachedThreadPool[Task]
    } yield HikariTransactor(DbConfig.dataSource, connectEC, transactEC)
  }

}
