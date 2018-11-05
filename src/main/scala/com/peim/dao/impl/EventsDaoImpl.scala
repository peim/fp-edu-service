package com.peim.dao.impl

import java.time.Instant
import java.util.UUID

import com.peim.dao._
import com.peim.models.tables.EventEntity
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0
import com.peim.models.api.in.CreateEvent

class EventsDaoImpl extends EventsDao {

  override def create(event: CreateEvent): Update0 = {
    val newId   = UUID.randomUUID()
    val created = Instant.now()
    sql"""insert into events (id, payload, user_id, type, created, source)
         |  values ($newId, ${event.payload}, ${event.userId}, ${event.`type`}, $created, ${event.source})""".stripMargin.update
  }

  override def find(id: UUID): Query0[EventEntity] =
    sql"select id, payload, user_id, type, created, source from events where id = $id".query

  override def findByUser(userId: Int): Query0[EventEntity] =
    sql"select id, payload, user_id, type, created, source from events where user_id = $userId".query

  override def list(skip: Int, take: Int): Query0[EventEntity] =
    sql"select id, payload, user_id, type, created, source from events offset $skip limit $take".query

}
