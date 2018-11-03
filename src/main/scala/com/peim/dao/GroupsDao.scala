package com.peim.dao

import com.peim.models.request.{CreateGroup, UpdateGroup}
import com.peim.models.tables.GroupEntity
import doobie.util.query.Query0
import doobie.util.update.Update0

trait GroupsDao {

  def create(group: CreateGroup): Update0

  def update(group: UpdateGroup): Update0

  def find(id: Int): Query0[GroupEntity]

  def list(skip: Int, take: Int): Query0[GroupEntity]

}
