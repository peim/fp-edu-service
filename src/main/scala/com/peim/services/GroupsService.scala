package com.peim.services

import cats.effect.IO
import com.peim.dao.GroupsDao
import com.peim.models.api.in.{CreateGroup, UpdateGroup}
import com.peim.models.tables.GroupEntity
import doobie.hikari.HikariTransactor
import doobie.implicits._

class GroupsService(groupsDao: GroupsDao, transactor: HikariTransactor[IO]) {

  def findGroup(groupId: Int): IO[Option[GroupEntity]] = {
    val query = for {
      q <- groupsDao.find(groupId).option
    } yield q
    query.transact(transactor)
  }

  def listGroups(skipOpt: Option[Int], takeOpt: Option[Int]): IO[Seq[GroupEntity]] = {
    val skip = skipOpt.getOrElse(0)
    val take = takeOpt.getOrElse(25)
    val query = for {
      q <- groupsDao.list(skip, take).to[Seq]
    } yield q
    query.transact(transactor)
  }

  def groupsHierarchy(): IO[Seq[GroupEntity]] = ???

  def createGroup(group: CreateGroup): IO[Int] = {
    val query = for {
      q <- groupsDao.create(group).withUniqueGeneratedKeys[Int]("id")
    } yield q
    query.transact(transactor)
  }

  def updateGroup(group: UpdateGroup): IO[Int] = {
    val query = for {
      q <- groupsDao.update(group).withUniqueGeneratedKeys[Int]("id")
    } yield q
    query.transact(transactor)
  }

}
