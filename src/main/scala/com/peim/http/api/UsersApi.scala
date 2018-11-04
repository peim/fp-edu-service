package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.models.api.in.{CreateUser, UpdateUser}
import com.peim.services.UsersService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

class UsersApi(usersService: UsersService) {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("users") {
        pathPrefix("get") {
          parameters('id.as[Int]) { userId =>
            // GET /fp-edu/v1/users/get
            get {
              onSuccess(usersService.findUser(userId)) {
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
                onSuccess(usersService.findUsersByGroup(groupId)) { list =>
                  complete(list)
                }
              }
            }
          } ~
          pathPrefix("list") {
            parameters('skip.as[Int].?, 'take.as[Int].?) { (skip, take) =>
              // GET /fp-edu/v1/users/list
              get {
                onSuccess(usersService.listUsers(skip, take)) { list =>
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
                  onSuccess(usersService.createUser(user)) { response =>
                    complete(response)
                  }
                }
              }
            }
          } ~
          pathPrefix("update") {
            pathEndOrSingleSlash {
              // PUT /fp-edu/v1/users/update
              put {
                entity(as[UpdateUser]) { user =>
                  onSuccess(usersService.updateUser(user)) { response =>
                    complete(response)
                  }
                }
              }
            }
          }
      }
    }

}
