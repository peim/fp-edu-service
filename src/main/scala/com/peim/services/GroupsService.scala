package com.peim.services

import cats.effect.{Async, Resource}
import com.peim.dao.GroupsDao
import com.peim.models.api.in.{CreateGroup, UpdateGroup}
import com.peim.models.tables.GroupEntity
import doobie.hikari.HikariTransactor
import doobie.implicits._

class GroupsService[F[_]: Async](groupsDao: GroupsDao, transactor: Resource[F, HikariTransactor[F]]) {

  def findGroup(groupId: Int): F[Option[GroupEntity]] = {
    transactor
      .use { xa =>
        groupsDao
          .find(groupId)
          .option
          .transact(xa)

      }
  }

  def listGroups(skipOpt: Option[Int], takeOpt: Option[Int]): F[Seq[GroupEntity]] = {
    transactor
      .use { xa =>
        val skip = skipOpt.getOrElse(0)
        val take = takeOpt.getOrElse(25)
        groupsDao
          .list(skip, take)
          .to[Seq]
          .transact(xa)
      }
  }

  def groupsHierarchy: F[Seq[GroupEntity]] = ???

  def createGroup(group: CreateGroup): F[Int] = {
    transactor
      .use { xa =>
        groupsDao
          .create(group)
          .withUniqueGeneratedKeys[Int]("id")
          .transact(xa)
      }
  }

  def updateGroup(group: UpdateGroup): F[Int] = {
    transactor
      .use { xa =>
        groupsDao
          .update(group)
          .withUniqueGeneratedKeys[Int]("id")
          .transact(xa)
      }
  }

}
