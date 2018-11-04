package com.peim.services

import cats.effect.{IO, Resource}
import com.peim.dao.UsersDao
import com.peim.models.api.in.{CreateUser, UpdateUser}
import com.peim.models.tables.UserEntity
import doobie.hikari.HikariTransactor
import doobie.implicits._

import scala.concurrent.Future

class UsersService(usersDao: UsersDao, transactor: Resource[IO, HikariTransactor[IO]]) {

  def findUser(userId: Int): Future[Option[UserEntity]] = {
    transactor
      .use { xa =>
        usersDao
          .find(userId)
          .option
          .transact(xa)

      }
      .unsafeToFuture()
  }

  def findUsersByGroup(groupId: Int): Future[Seq[UserEntity]] = {
    transactor
      .use { xa =>
        usersDao
          .findByGroup(groupId)
          .to[Seq]
          .transact(xa)

      }
      .unsafeToFuture()
  }

  def listUsers(skipOpt: Option[Int], takeOpt: Option[Int]): Future[Seq[UserEntity]] = {
    transactor
      .use { xa =>
        val skip = skipOpt.getOrElse(0)
        val take = takeOpt.getOrElse(25)
        usersDao
          .list(skip, take)
          .to[Seq]
          .transact(xa)
      }
      .unsafeToFuture()
  }

  def createUser(user: CreateUser): Future[Int] = {
    transactor
      .use { xa =>
        usersDao
          .create(user)
          .withUniqueGeneratedKeys[Int]("id")
          .transact(xa)
      }
      .unsafeToFuture()
  }

  def updateUser(user: UpdateUser): Future[Int] = {
    transactor
      .use { xa =>
        usersDao
          .update(user)
          .withUniqueGeneratedKeys[Int]("id")
          .transact(xa)
      }
      .unsafeToFuture()
  }

}
