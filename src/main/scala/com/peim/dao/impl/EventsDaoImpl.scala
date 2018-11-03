package com.peim.dao.impl

import java.time.Instant
import java.util.UUID

import com.peim.dao.EventsDao
import com.peim.models.request.CreateEvent
import com.peim.models.tables.EventEntity
import doobie.implicits._
import doobie.util.Meta
import doobie.util.query.Query0
import doobie.util.update.Update0
import io.circe._
import cats.syntax.either._
import com.peim.models.{EventType, Payload}
import io.circe.generic.auto._
import io.circe.syntax._

class EventsDaoImpl extends EventsDao {

  implicit val uuidMeta: Meta[UUID]           = Meta[String].timap(e => UUID.fromString(e))(_.toString)
  implicit val eventTypeMeta: Meta[EventType] = Meta[String].timap(EventType.fromString)(EventType.toString)

  implicit val jsonbMeta: Meta[Payload] = {
    val fromString = (jsonStr: String) =>
      parser.parse(jsonStr).leftMap[Json](err => throw err).merge.as[Payload] match {
        case Right(result) => result
        case Left(error)   => throw error
    }
    val toString = (payload: Payload) => payload.asJson.toString
    Meta[String].timap[Payload](fromString)(toString)
  }

  override def create(event: CreateEvent): Update0 = {
    val newId   = UUID.randomUUID()
    val created = Instant.now()
    sql"""insert into events (id, payload, user_id, type, created, source)
         |  values ($newId, ${event.payload}, ${event.userId}, ${event.`type`}, $created, ${event.source})""".stripMargin.update
  }

  override def find(id: Int): Query0[EventEntity] =
    sql"select id, payload, user_id, type, created, source from events where id = $id".query

  override def list(skip: Int, take: Int): Query0[EventEntity] =
    sql"select id, payload, user_id, type, created, source from events offset $skip limit $take".query

}
