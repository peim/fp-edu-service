package com.peim.services

import cats.effect.{Async, Resource}
import com.peim.dao.UsersDao
import com.peim.models.api.in.{CreateUser, UpdateUser}
import com.peim.models.tables.UserEntity
import doobie.hikari.HikariTransactor
import doobie.implicits._

class UsersService[F[_]: Async](usersDao: UsersDao, transactor: Resource[F, HikariTransactor[F]]) {

  def findUser(userId: Int): F[Option[UserEntity]] = {
    transactor
      .use { xa =>
        usersDao
          .find(userId)
          .option
          .transact(xa)

      }
  }

  def findUsersByGroup(groupId: Int): F[Seq[UserEntity]] = {
    transactor
      .use { xa =>
        usersDao
          .findByGroup(groupId)
          .to[Seq]
          .transact(xa)

      }
  }

  def listUsers(skipOpt: Option[Int], takeOpt: Option[Int]): F[Seq[UserEntity]] = {
    transactor
      .use { xa =>
        val skip = skipOpt.getOrElse(0)
        val take = takeOpt.getOrElse(25)
        usersDao
          .list(skip, take)
          .to[Seq]
          .transact(xa)
      }
  }

  def createUser(user: CreateUser): F[Int] = {
    transactor
      .use { xa =>
        usersDao
          .create(user)
          .withUniqueGeneratedKeys[Int]("id")
          .transact(xa)
      }
  }

  def updateUser(user: UpdateUser): F[Int] = {
    transactor
      .use { xa =>
        usersDao
          .update(user)
          .withUniqueGeneratedKeys[Int]("id")
          .transact(xa)
      }
  }

}
