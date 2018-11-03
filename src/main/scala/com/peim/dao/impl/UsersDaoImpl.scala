package com.peim.dao.impl

import com.peim.dao.UsersDao
import com.peim.models.api.in.{CreateUser, UpdateUser}
import com.peim.models.tables.UserEntity
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0

class UsersDaoImpl extends UsersDao {

  override def create(user: CreateUser): Update0 =
    sql"""insert into users (name, group_id, email, phone)
         |  values (${user.name}, ${user.groupId}, ${user.email}, ${user.phone})""".stripMargin.update

  override def update(user: UpdateUser): Update0 = {
    val upateField = Seq(
      user.name.map(v => fr"set name = $v"),
      user.groupId.map(v => fr"set group_id = $v"),
      user.email.map(v => fr"set email = $v"),
      user.phone.map(v => fr"set phone = $v")
    ).flatten.foldLeft(fr"") {
      case (acc, e) =>
        if (acc.toString.isEmpty) acc ++ e
        else acc ++ e ++ fr","
    }
    (fr"update users" ++ upateField ++ fr"where id = ${user.id}").update
  }

  override def find(id: Int): Query0[UserEntity] =
    sql"select id, name, group_id, email, phone from users where id = $id".query

  override def list(skip: Int, take: Int): Query0[UserEntity] =
    sql"select id, name, group_id, email, phone from users offset $skip limit $take".query

}
