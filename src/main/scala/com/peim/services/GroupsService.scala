package com.peim.services

import cats.effect.{IO, Resource}
import com.peim.dao.GroupsDao
import com.peim.models.api.in.{CreateGroup, UpdateGroup}
import com.peim.models.tables.GroupEntity
import doobie.hikari.HikariTransactor
import doobie.implicits._

import scala.concurrent.Future

class GroupsService(groupsDao: GroupsDao, transactor: Resource[IO, HikariTransactor[IO]]) {

  def findGroup(groupId: Int): Future[Option[GroupEntity]] = {
    transactor
      .use { xa =>
        groupsDao
          .find(groupId)
          .option
          .transact(xa)

      }
      .unsafeToFuture()
  }

  def listGroups(skipOpt: Option[Int], takeOpt: Option[Int]): Future[Seq[GroupEntity]] = {
    transactor
      .use { xa =>
        val skip = skipOpt.getOrElse(0)
        val take = takeOpt.getOrElse(25)
        groupsDao
          .list(skip, take)
          .to[Seq]
          .transact(xa)
      }
      .unsafeToFuture()
  }

  def groupsHierarchy(): Future[Seq[GroupEntity]] = ???

  def createGroup(group: CreateGroup): Future[Int] = {
    transactor
      .use { xa =>
        groupsDao
          .create(group)
          .withUniqueGeneratedKeys[Int]("id")
          .transact(xa)
      }
      .unsafeToFuture()
  }

  def updateGroup(group: UpdateGroup): Future[Int] = {
    transactor
      .use { xa =>
        groupsDao
          .update(group)
          .withUniqueGeneratedKeys[Int]("id")
          .transact(xa)
      }
      .unsafeToFuture()
  }

}
