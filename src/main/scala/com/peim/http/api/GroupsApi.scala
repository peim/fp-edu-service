package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.effect.Async
import com.peim.models.api.in.{CreateGroup, UpdateGroup}
import com.peim.services.GroupsService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import cats.effect._
import com.peim.http.FutureConversion

class GroupsApi[F[_]: Async](groupsService: GroupsService[F])(implicit fc: FutureConversion[F]) {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("groups") {
        path("get") {
          parameters('id.as[Int]) { groupId =>
            // GET /fp-edu/v1/groups/get
            get {
              onSuccess(fc.toFuture(groupsService.findGroup(groupId))) {
                case Some(group) => complete(group)
                case None        => complete(NotFound)
              }
            }
          }
        } ~
          path("list") {
            parameters('skip.as[Int].?, 'take.as[Int].?) { (skip, take) =>
              // GET /fp-edu/v1/groups/list
              get {
                onSuccess(fc.toFuture(groupsService.listGroups(skip, take))) { list =>
                  complete(list)
                }
              }
            }
          } ~
          path("hierarchy") {
            // GET /fp-edu/v1/groups/hierarchy
            get {
              onSuccess(fc.toFuture(groupsService.groupsHierarchy)) { hierarchy =>
                complete(hierarchy)
              }
            }
          } ~
          path("create") {
            // POST /fp-edu/v1/groups/create
            post {
              entity(as[CreateGroup]) { group =>
                onSuccess(fc.toFuture(groupsService.createGroup(group))) { response =>
                  complete(response)
                }
              }
            }
          } ~
          path("update") {
            // PUT /fp-edu/v1/groups/update
            put {
              entity(as[UpdateGroup]) { group =>
                onSuccess(fc.toFuture(groupsService.updateGroup(group))) { response =>
                  complete(response)
                }
              }
            }
          }
      }
    }

}
