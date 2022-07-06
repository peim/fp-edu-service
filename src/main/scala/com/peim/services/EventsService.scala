package com.peim.services

import com.peim.dao.EventsDao
import com.peim.models.api.in.CreateEvent
import com.peim.models.tables.EventEntity
import zio._

object EventsService {

  case class Service(eventsDao: EventsDao.Service) {
    def findEvent(eventId: String): Task[EventEntity] = {
      eventsDao.find(eventId)
    }

    def findByUser(userId: Int): Task[List[EventEntity]] = {
      eventsDao.findByUser(userId)
    }

    def listEvents(skip: Int, take: Int): Task[List[EventEntity]] = {
      eventsDao.list(skip, take)
    }

    def createEvent(event: CreateEvent): Task[String] = {
      eventsDao.create(event)
    }
  }

  val live: ZLayer[EventsDao.Service, Throwable, EventsService.Service] =
    ZManaged.service[EventsDao.Service]
      .flatMap(dao => ZManaged.succeed(Service(dao)))
      .toLayer

  def findEvent(eventId: String): ZIO[Service, Throwable, EventEntity] =
    ZIO.serviceWithZIO[EventsService.Service](_.findEvent(eventId))

  def findEventByUser(userId: Int): ZIO[Service, Throwable, List[EventEntity]] =
    ZIO.serviceWithZIO[EventsService.Service](_.findByUser(userId))

  def listEvents(skip: Int, take: Int): ZIO[Service, Throwable, List[EventEntity]] =
    ZIO.serviceWithZIO[EventsService.Service](_.listEvents(skip, take))

  def createEvent(event: CreateEvent): ZIO[Service, Throwable, String] =
    ZIO.serviceWithZIO[EventsService.Service](_.createEvent(event))

}
