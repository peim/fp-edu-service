package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.models.api.in.{CreateGroup, UpdateGroup}
import com.peim.services.GroupsService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import monix.eval.Task
import monix.execution.Scheduler

class GroupsApi(groupsService: GroupsService[Task])(implicit s: Scheduler) {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("groups") {
        path("get") {
          parameters('id.as[Int]) { groupId =>
            // GET /fp-edu/v1/groups/get
            get {
              onSuccess(groupsService.findGroup(groupId).runAsync) {
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
                onSuccess(groupsService.listGroups(skip, take).runAsync) { list =>
                  complete(list)
                }
              }
            }
          } ~
          path("hierarchy") {
            // GET /fp-edu/v1/groups/hierarchy
            get {
              onSuccess(groupsService.groupsHierarchy.runAsync) { hierarchy =>
                complete(hierarchy)
              }
            }
          } ~
          path("create") {
            // POST /fp-edu/v1/groups/create
            post {
              entity(as[CreateGroup]) { group =>
                onSuccess(groupsService.createGroup(group).runAsync) { response =>
                  complete(response)
                }
              }
            }
          } ~
          path("update") {
            // PUT /fp-edu/v1/groups/update
            put {
              entity(as[UpdateGroup]) { group =>
                onSuccess(groupsService.updateGroup(group).runAsync) { response =>
                  complete(response)
                }
              }
            }
          }
      }
    }

}
