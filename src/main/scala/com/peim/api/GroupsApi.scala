package com.peim.api

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.models.api.in.{CreateGroup, UpdateGroup}
import com.peim.models.tables.GroupEntity
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.Future

class GroupsApi() {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("groups") {
        pathPrefix("get") {
          parameters('id.as[Int]) { groupId =>
            // GET /fp-edu/v1/groups/get
            get {
              onSuccess(findGroup(groupId)) {
                case Some(group) => complete(group)
                case None        => complete(NotFound)
              }
            }
          }
        } ~
          pathPrefix("list") {
            parameters('take.as[Int].?, 'skip.as[Int].?) { (take, skip) =>
              // GET /fp-edu/v1/groups/list
              get {
                onSuccess(listGroups(take, skip)) { list =>
                  complete(list)
                }
              }
            }
          } ~
          pathPrefix("hierarchy") {
            pathEndOrSingleSlash {
              // GET /fp-edu/v1/groups/hierarchy
              get {
                onSuccess(groupsHierarchy) { hierarchy =>
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
                  onSuccess(createGroup(group)) { response =>
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
                  onSuccess(updateGroup(group)) { response =>
                    complete(response)
                  }
                }
              }
            }
          }
      }
    }

  private def findGroup(groupId: Int): Future[Option[GroupEntity]]                             = ???
  private def listGroups(takeOpt: Option[Int], skipOpt: Option[Int]): Future[Seq[GroupEntity]] = ???
  private def groupsHierarchy(): Future[Seq[GroupEntity]]                                      = ???
  private def createGroup(group: CreateGroup): Future[Option[Int]]                             = ???
  private def updateGroup(group: UpdateGroup): Future[Option[Int]]                             = ???

}
