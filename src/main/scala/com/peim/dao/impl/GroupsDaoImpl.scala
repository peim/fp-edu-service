package com.peim.dao.impl

import com.peim.dao.GroupsDao
import com.peim.models.api.in.{CreateGroup, UpdateGroup}
import com.peim.models.tables.GroupEntity
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0

class GroupsDaoImpl extends GroupsDao {

  override def create(group: CreateGroup): Update0 =
    sql"""insert into groups (name, type, parent_id)
         |  values (${group.name}, ${group.`type`}, ${group.parentId})""".stripMargin.update

  override def update(group: UpdateGroup): Update0 = {
    val upateField = Seq(
      group.name.map(v => fr"set name = $v"),
      group.`type`.map(v => fr"set type = $v"),
      group.parentId.map(v => fr"set parent_id = $v")
    ).flatten.foldLeft(fr"") {
      case (acc, e) =>
        if (acc.toString.isEmpty) acc ++ e
        else acc ++ e ++ fr","
    }
    (fr"update groups" ++ upateField ++ fr"where id = ${group.id}").update
  }

  override def find(id: Int): Query0[GroupEntity] =
    sql"select id, name, type, parent_id from groups where id = $id".query

  override def list(skip: Int, take: Int): Query0[GroupEntity] =
    sql"select id, name, type, parent_id from groups offset $skip limit $take".query

}
