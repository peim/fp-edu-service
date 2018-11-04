package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.models.api.in.{CreateGroup, UpdateGroup}
import com.peim.services.GroupsService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

class GroupsApi(groupsService: GroupsService) {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("groups") {
        pathPrefix("get") {
          parameters('id.as[Int]) { groupId =>
            // GET /fp-edu/v1/groups/get
            get {
              onSuccess(groupsService.findGroup(groupId)) {
                case Some(group) => complete(group)
                case None        => complete(NotFound)
              }
            }
          }
        } ~
          pathPrefix("list") {
            parameters('skip.as[Int].?, 'take.as[Int].?) { (skip, take) =>
              // GET /fp-edu/v1/groups/list
              get {
                onSuccess(groupsService.listGroups(skip, take)) { list =>
                  complete(list)
                }
              }
            }
          } ~
          pathPrefix("hierarchy") {
            pathEndOrSingleSlash {
              // GET /fp-edu/v1/groups/hierarchy
              get {
                onSuccess(groupsService.groupsHierarchy) { hierarchy =>
                  complete(hierarchy)
                }
              }
            }
          } ~
          pathPrefix("create") {
            pathEndOrSingleSlash {
              // POST /fp-edu/v1/groups/create
              post {
                entity(as[CreateGroup]) { group =>
                  onSuccess(groupsService.createGroup(group)) { response =>
                    complete(response)
                  }
                }
              }
            }
          } ~
          pathPrefix("update") {
            pathEndOrSingleSlash {
              // PUT /fp-edu/v1/groups/update
              post {
                entity(as[UpdateGroup]) { group =>
                  onSuccess(groupsService.updateGroup(group)) { response =>
                    complete(response)
                  }
                }
              }
            }
          }
      }
    }

}
