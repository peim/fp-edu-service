package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.models.api.in.{CreateUser, UpdateUser}
import com.peim.models.tables.UserEntity
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.Future

class UsersApi() {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("users") {
        pathPrefix("get") {
          parameters('id.as[Int]) { userId =>
            // GET /fp-edu/v1/users/get
            get {
              onSuccess(findUser(userId)) {
                case Some(user) => complete(user)
                case None       => complete(NotFound)
              }
            }
          }
        } ~
          pathPrefix("getByGroup") {
            parameters('groupId.as[Int]) { groupId =>
              // GET /fp-edu/v1/users/getByGroup
              get {
                onSuccess(findUsersByGroup(groupId)) { list =>
                  complete(list)
                }
              }
            }
          } ~
          pathPrefix("list") {
            parameters('take.as[Int].?, 'skip.as[Int].?) { (take, skip) =>
              // GET /fp-edu/v1/users/list
              get {
                onSuccess(listUsers(take, skip)) { list =>
                  complete(list)
                }
              }
            }
          } ~
          pathPrefix("create") {
            pathEndOrSingleSlash {
              // POST /fp-edu/v1/users/create
              post {
                entity(as[CreateUser]) { user =>
                  onSuccess(createUser(user)) { response =>
                    complete(response)
                  }
                }
              }
            }
          } ~
          pathPrefix("update") {
            pathEndOrSingleSlash {
              // PUT /fp-edu/v1/users/update
              post {
                entity(as[UpdateUser]) { user =>
                  onSuccess(updateUser(user)) { response =>
                    complete(response)
                  }
                }
              }
            }
          }
      }
    }

  private def findUser(userId: Int): Future[Option[UserEntity]]                              = ???
  private def findUsersByGroup(groupId: Int): Future[Seq[UserEntity]]                        = ???
  private def listUsers(takeOpt: Option[Int], skipOpt: Option[Int]): Future[Seq[UserEntity]] = ???
  private def createUser(group: CreateUser): Future[Option[Int]]                             = ???
  private def updateUser(group: UpdateUser): Future[Option[Int]]                             = ???

}
