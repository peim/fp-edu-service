package com.peim.config

import java.time.Instant
import java.util.UUID

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.hikari._
import com.peim.dao.impl._
import com.peim.models.{Info, Payload, Public}
import com.peim.models.request.{CreateEvent, CreateGroup, CreateUser}
import doobie.util.Meta

import scala.concurrent.ExecutionContext

object DatabaseConfig {

  implicit val cs = IO.contextShift(ExecutionContext.global)

  val transactor: Resource[IO, HikariTransactor[IO]] =
    for {
      awaitConnectionsEC <- ExecutionContexts.fixedThreadPool[IO](8)
      executeQueriesEC   <- ExecutionContexts.cachedThreadPool[IO]
      transactor <- HikariTransactor.newHikariTransactor[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost:5432/edu_db",
        "edu_user",
        "edu_password",
        awaitConnectionsEC,
        executeQueriesEC
      )
    } yield transactor

  val program =
    transactor.use { xa =>
      val g = new GroupsDaoImpl
      val u = new UsersDaoImpl
      val e = new EventsDaoImpl

      implicit val uuidMeta: Meta[UUID] = Meta[String].timap(e => UUID.fromString(e))(_.toString)

      val newGroup = CreateGroup("test-group", Public, 0)
      val query = for {
        groupId <- g.create(newGroup).withUniqueGeneratedKeys[Int]("id")
        newUser = CreateUser("test-user", groupId, s"user-$groupId@test.com", None)
        userId <- u.create(newUser).withUniqueGeneratedKeys[Int]("id")
        payload  = Payload("a", None, true, 1, Instant.now)
        newEvent = CreateEvent(payload, userId, Info, None)
        eventId <- e.create(newEvent).withUniqueGeneratedKeys[UUID]("id")
      } yield (groupId, userId, eventId)
      query.transact(xa)
    }

}