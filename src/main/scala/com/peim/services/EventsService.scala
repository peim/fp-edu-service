package com.peim.services

import java.util.UUID

import cats.effect.{Async, Resource}
import com.peim.dao._
import com.peim.models.api.in.CreateEvent
import com.peim.models.tables.EventEntity
import doobie.hikari.HikariTransactor
import doobie.implicits._

class EventsService[F[_]: Async](eventsDao: EventsDao, transactor: Resource[F, HikariTransactor[F]]) {

  def findEvent(eventId: UUID): F[Option[EventEntity]] = {
    transactor
      .use { xa =>
        eventsDao
          .find(eventId)
          .option
          .transact(xa)
      }
  }

  def findEventsByUser(userId: Int): F[Seq[EventEntity]] = {
    transactor
      .use { xa =>
        eventsDao
          .findByUser(userId)
          .to[Seq]
          .transact(xa)
      }
  }

  def listEvents(skipOpt: Option[Int], takeOpt: Option[Int]): F[Seq[EventEntity]] = {
    transactor
      .use { xa =>
        val skip = skipOpt.getOrElse(0)
        val take = takeOpt.getOrElse(25)
        eventsDao
          .list(skip, take)
          .to[Seq]
          .transact(xa)
      }
  }

  def createEvent(event: CreateEvent): F[UUID] = {
    transactor
      .use { xa =>
        eventsDao
          .create(event)
          .withUniqueGeneratedKeys[UUID]("id")
          .transact(xa)
      }
  }

}
