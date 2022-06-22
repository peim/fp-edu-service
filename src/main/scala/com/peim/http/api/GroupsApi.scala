//package com.peim.http.api
//
//import akka.http.scaladsl.model.StatusCodes.NotFound
//import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.server.Route
//import cats.effect.Sync
//import com.peim.models.api.in.{CreateGroup, UpdateGroup}
//import com.peim.services.GroupsService
//import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//import io.circe.generic.auto._
//import cats.effect._
//import com.peim.utils.ToFutureConversion
//
//class GroupsApi[F[_]: Sync](groupsService: GroupsService[F])(implicit tfc: ToFutureConversion[F]) {
//
//  def routes: Route =
//    pathPrefix("v1") {
//      pathPrefix("groups") {
//        path("get") {
//          parameters('id.as[Int]) { groupId =>
//            // GET /fp-edu/v1/groups/get
//            get {
//              onSuccess(tfc.toFuture(groupsService.findGroup(groupId))) {
//                case Some(group) => complete(group)
//                case None        => complete(NotFound)
//              }
//            }
//          }
//        } ~
//          path("list") {
//            parameters('skip.as[Int].?, 'take.as[Int].?) { (skip, take) =>
//              // GET /fp-edu/v1/groups/list
//              get {
//                onSuccess(tfc.toFuture(groupsService.listGroups(skip, take))) { list =>
//                  complete(list)
//                }
//              }
//            }
//          } ~
//          path("hierarchy") {
//            parameter('rootId.as[Int].?) { rootId =>
//              // GET /fp-edu/v1/groups/hierarchy
//              get {
//                onSuccess(tfc.toFuture(groupsService.groupsHierarchy(rootId))) {
//                  case Some(hierarchy) => complete(hierarchy)
//                  case None            => complete(NotFound)
//                }
//              }
//            }
//          } ~
//          path("create") {
//            // POST /fp-edu/v1/groups/create
//            post {
//              entity(as[CreateGroup]) { group =>
//                onSuccess(tfc.toFuture(groupsService.createGroup(group))) { response =>
//                  complete(response)
//                }
//              }
//            }
//          } ~
//          path("update") {
//            // PUT /fp-edu/v1/groups/update
//            put {
//              entity(as[UpdateGroup]) { group =>
//                onSuccess(tfc.toFuture(groupsService.updateGroup(group))) { response =>
//                  complete(response)
//                }
//              }
//            }
//          }
//      }
//    }
//
//}
