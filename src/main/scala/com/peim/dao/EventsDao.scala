package com.peim.dao

import com.peim.models.api.in.CreateEvent
import com.peim.models.tables.EventEntity
import zio.{Task, ZLayer}

object EventsDao {

  trait Service {
    def create(event: CreateEvent): Task[String]
    def find(id: String): Task[EventEntity]
    def findByUser(userId: Int): Task[List[EventEntity]]
    def list(skip: Int, take: Int): Task[List[EventEntity]]
  }

  val live: ZLayer[Any, Nothing, EventsDao.Service] = ZLayer.succeed(new Service {
    override def create(event: CreateEvent): Task[String] = ???

    override def find(id: String): Task[EventEntity] = ???

    override def findByUser(userId: Int): Task[List[EventEntity]] = ???

    override def list(skip: Int, take: Int): Task[List[EventEntity]] = ???
  })
}
