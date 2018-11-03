package com.peim.dao

import com.peim.models.request.CreateEvent
import com.peim.models.tables.EventEntity
import doobie.util.query.Query0
import doobie.util.update.Update0

trait EventsDao {

  def create(event: CreateEvent): Update0

  def find(id: Int): Query0[EventEntity]

  def list(skip: Int, take: Int): Query0[EventEntity]

}
