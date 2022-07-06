//package com.peim.dao
//
//import java.util.UUID
//
//import com.peim.models.api.in.CreateEvent
//import com.peim.models.tables.EventEntity
//import doobie.util.query.Query0
//import doobie.util.update.Update0
//
//trait EventsDao {
//
//  def create(event: CreateEvent): Update0
//
//  def find(id: UUID): Query0[EventEntity]
//
//  def findByUser(userId: Int): Query0[EventEntity]
//
//  def list(skip: Int, take: Int): Query0[EventEntity]
//
//}
