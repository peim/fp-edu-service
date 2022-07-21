package com.peim.dao

import com.peim.config.AppConfig
import doobie.ExecutionContexts
import doobie.h2.H2Transactor
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio._

object Database {

  val live: ZLayer[Scope with AppConfig, Throwable, H2Transactor[Task]] = {
    ZLayer.scoped {
      for {
        result <- (for {
          ce <- ExecutionContexts.fixedThreadPool[Task](32)
          xa <- H2Transactor.newH2Transactor[Task](
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            "sa",
            "",
            ce,
          )
        } yield xa).use(xa => ZIO.succeed(xa))
      } yield result
    }
  }
}
