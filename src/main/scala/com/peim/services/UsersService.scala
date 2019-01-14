package com.peim.services

import cats.effect.Sync
import com.peim.dao.UsersDao
import com.peim.models.api.in.{CreateUser, UpdateUser}
import com.peim.models.tables.UserEntity
import doobie.hikari.HikariTransactor
import doobie.implicits._

class UsersService[F[_]: Sync](usersDao: UsersDao, transactor: HikariTransactor[F]) {

  def findUser(userId: Int): F[Option[UserEntity]] = {
    usersDao
      .find(userId)
      .option
      .transact(transactor)
  }

  def findUsersByGroup(groupId: Int): F[Seq[UserEntity]] = {
    usersDao
      .findByGroup(groupId)
      .to[Seq]
      .transact(transactor)
  }

  def listUsers(skipOpt: Option[Int], takeOpt: Option[Int]): F[Seq[UserEntity]] = {
    val skip = skipOpt.getOrElse(0)
    val take = takeOpt.getOrElse(25)
    usersDao
      .list(skip, take)
      .to[Seq]
      .transact(transactor)
  }

  def createUser(user: CreateUser): F[Int] = {
    usersDao
      .create(user)
      .withUniqueGeneratedKeys[Int]("id")
      .transact(transactor)
  }

  def updateUser(user: UpdateUser): F[Int] = {
    usersDao
      .update(user)
      .withUniqueGeneratedKeys[Int]("id")
      .transact(transactor)
  }

}
