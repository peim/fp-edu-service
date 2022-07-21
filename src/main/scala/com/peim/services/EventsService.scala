package com.peim.services

import com.peim.clients.NotifyService
import com.peim.dao.EventsDao
import com.peim.models.api.in.CreateEvent
import com.peim.models.tables.EventEntity
import zio._

object EventsService {

  final case class Service(eventsDao: EventsDao.Service, notifyService: NotifyService.Service) {
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

  val live: ZLayer[EventsDao.Service with NotifyService.Service, Throwable, EventsService.Service] = {
    ZLayer {
      for {
        eventsDao <- ZIO.service[EventsDao.Service]
        notifyService <- ZIO.service[NotifyService.Service]
        service = Service(eventsDao, notifyService)
      } yield service
    }
  }

  def findEvent(eventId: String): ZIO[Service, Throwable, EventEntity] =
    ZIO.serviceWithZIO[EventsService.Service](_.findEvent(eventId))

  def findEventByUser(userId: Int): ZIO[Service, Throwable, List[EventEntity]] =
    ZIO.serviceWithZIO[EventsService.Service](_.findByUser(userId))

  def listEvents(skip: Int, take: Int): ZIO[Service, Throwable, List[EventEntity]] =
    ZIO.serviceWithZIO[EventsService.Service](_.listEvents(skip, take))

  def createEvent(event: CreateEvent): ZIO[Service, Throwable, String] =
    ZIO.serviceWithZIO[EventsService.Service](_.createEvent(event))

}
