//package com.peim.dao
//
//import com.peim.config.AppConfig
//import doobie.ExecutionContexts
//import doobie.h2.H2Transactor
//import zio.interop.catz._
//import zio.interop.catz.implicits._
//import zio._
//
//object Database {
//
//  val live = {//: ZLayer[Scope with AppConfig, Throwable, H2Transactor[Task]] = {
////      (for {
////        ce <- ExecutionContexts.fixedThreadPool[Task](32)
////        xa <- H2Transactor.newH2Transactor[Task](
////          "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
////          "sa",
////          "",
////          ce,
////        )
////      } yield xa).
////
//
//    ZLayer.scoped {
//      for {
////        _ <- ZIO.service[Scope]
//        _ <- ZIO.service[AppConfig]
//        result <- (for {
//          ce <- ExecutionContexts.fixedThreadPool[Task](32)
//          xa <- H2Transactor.newH2Transactor[Task](
//            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
//            "sa",
//            "",
//            ce,
//          )
//        } yield xa).use(ZIO.succeed)
//      } yield result
//
//      /*(for {
//        ce <- ExecutionContexts.fixedThreadPool[Task](32)
//        xa <- H2Transactor.newH2Transactor[Task](
//          "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
//          "sa",
//          "",
//          ce,
//        )
//      } yield xa).use(ZIO.succeed)*/
//    }
//  }
//}
