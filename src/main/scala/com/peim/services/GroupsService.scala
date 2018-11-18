package com.peim.services

import cats.effect.Async
import com.peim.dao.GroupsDao
import com.peim.models.api.in.{CreateGroup, UpdateGroup}
import com.peim.models.tables.GroupEntity
import doobie.hikari.HikariTransactor
import doobie.implicits._

class GroupsService[F[_]: Async](groupsDao: GroupsDao, transactor: HikariTransactor[F]) {

  def findGroup(groupId: Int): F[Option[GroupEntity]] = {
    groupsDao
      .find(groupId)
      .option
      .transact(transactor)
  }

  def listGroups(skipOpt: Option[Int], takeOpt: Option[Int]): F[Seq[GroupEntity]] = {
    val skip = skipOpt.getOrElse(0)
    val take = takeOpt.getOrElse(25)
    groupsDao
      .list(skip, take)
      .to[Seq]
      .transact(transactor)
  }

  def groupsHierarchy: F[Seq[GroupEntity]] = ???

  def createGroup(group: CreateGroup): F[Int] = {
    groupsDao
      .create(group)
      .withUniqueGeneratedKeys[Int]("id")
      .transact(transactor)
  }

  def updateGroup(group: UpdateGroup): F[Int] = {
    groupsDao
      .update(group)
      .withUniqueGeneratedKeys[Int]("id")
      .transact(transactor)
  }

}
