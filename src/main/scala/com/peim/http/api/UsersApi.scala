package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.effect.Async
import com.peim.models.api.in.{CreateUser, UpdateUser}
import com.peim.services.UsersService
import com.peim.utils.ToFutureConversion
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

class UsersApi[F[_]: Async](usersService: UsersService[F])(implicit tfc: ToFutureConversion[F]) {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("users") {
        path("get") {
          parameters('id.as[Int]) { userId =>
            // GET /fp-edu/v1/users/get
            get {
              onSuccess(tfc.toFuture(usersService.findUser(userId))) {
                case Some(user) => complete(user)
                case None       => complete(NotFound)
              }
            }
          }
        } ~
          path("getByGroup") {
            parameters('groupId.as[Int]) { groupId =>
              // GET /fp-edu/v1/users/getByGroup
              get {
                onSuccess(tfc.toFuture(usersService.findUsersByGroup(groupId))) { list =>
                  complete(list)
                }
              }
            }
          } ~
          path("list") {
            parameters('skip.as[Int].?, 'take.as[Int].?) { (skip, take) =>
              // GET /fp-edu/v1/users/list
              get {
                onSuccess(tfc.toFuture(usersService.listUsers(skip, take))) { list =>
                  complete(list)
                }
              }
            }
          } ~
          path("create") {
            // POST /fp-edu/v1/users/create
            post {
              entity(as[CreateUser]) { user =>
                onSuccess(tfc.toFuture(usersService.createUser(user))) { response =>
                  complete(response)
                }
              }
            }
          } ~
          path("update") {
            // PUT /fp-edu/v1/users/update
            put {
              entity(as[UpdateUser]) { user =>
                onSuccess(tfc.toFuture(usersService.updateUser(user))) { response =>
                  complete(response)
                }
              }
            }
          }
      }
    }

}
