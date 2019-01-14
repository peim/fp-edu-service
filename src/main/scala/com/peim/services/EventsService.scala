package com.peim.services

import java.util.UUID

import cats.effect.Sync
import com.peim.dao._
import com.peim.models.api.in.CreateEvent
import com.peim.models.tables.EventEntity
import doobie.hikari.HikariTransactor
import doobie.implicits._

class EventsService[F[_]: Sync](eventsDao: EventsDao, transactor: HikariTransactor[F]) {

  def findEvent(eventId: UUID): F[Option[EventEntity]] = {
    eventsDao
      .find(eventId)
      .option
      .transact(transactor)
  }

  def findEventsByUser(userId: Int): F[Seq[EventEntity]] = {
    eventsDao
      .findByUser(userId)
      .to[Seq]
      .transact(transactor)
  }

  def listEvents(skipOpt: Option[Int], takeOpt: Option[Int]): F[Seq[EventEntity]] = {
    val skip = skipOpt.getOrElse(0)
    val take = takeOpt.getOrElse(25)
    eventsDao
      .list(skip, take)
      .to[Seq]
      .transact(transactor)
  }

  def createEvent(event: CreateEvent): F[UUID] = {
    eventsDao
      .create(event)
      .withUniqueGeneratedKeys[UUID]("id")
      .transact(transactor)
  }

}
