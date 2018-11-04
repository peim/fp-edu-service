package com.peim.services

import java.util.UUID

import cats.effect.{IO, Resource}
import com.peim.dao.EventsDao
import com.peim.models.api.in.CreateEvent
import com.peim.models.tables.EventEntity
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.Meta

import scala.concurrent.Future

class EventsService(eventsDao: EventsDao, transactor: Resource[IO, HikariTransactor[IO]]) {

  // todo
  implicit val uuidMeta: Meta[UUID] = Meta[String].timap(e => UUID.fromString(e))(_.toString)

  def findEvent(eventId: UUID): Future[Option[EventEntity]] = {
    transactor
      .use { xa =>
        eventsDao
          .find(eventId)
          .option
          .transact(xa)
      }
      .unsafeToFuture()
  }

  def findEventsByUser(userId: Int): Future[Seq[EventEntity]] = {
    transactor
      .use { xa =>
        eventsDao
          .findByUser(userId)
          .to[Seq]
          .transact(xa)
      }
      .unsafeToFuture()
  }

  def listEvents(skipOpt: Option[Int], takeOpt: Option[Int]): Future[Seq[EventEntity]] = {
    transactor
      .use { xa =>
        val skip = skipOpt.getOrElse(0)
        val take = takeOpt.getOrElse(25)
        eventsDao
          .list(skip, take)
          .to[Seq]
          .transact(xa)
      }
      .unsafeToFuture()
  }

  def createEvent(event: CreateEvent): Future[UUID] = {
    transactor
      .use { xa =>
        eventsDao
          .create(event)
          .withUniqueGeneratedKeys[UUID]("id")
          .transact(xa)
      }
      .unsafeToFuture()
  }

}
