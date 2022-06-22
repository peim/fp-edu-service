//package com.peim.http.api
//
//import java.util.UUID
//
//import akka.http.scaladsl.model.StatusCodes.NotFound
//import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.server.Route
//import cats.effect.Sync
//import com.peim.models.api.in.CreateEvent
//import com.peim.services.EventsService
//import com.peim.utils.ToFutureConversion
//import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
//import io.circe.generic.auto._
//
//class EventsApi[F[_]: Sync](eventsService: EventsService[F])(implicit tfc: ToFutureConversion[F]) {
//
//  def routes: Route =
//    pathPrefix("v1") {
//      pathPrefix("events") {
//        path("get") {
//          parameters('id.as[String]) { eventId =>
//            // GET /fp-edu/v1/events/get
//            get {
//              onSuccess(tfc.toFuture(eventsService.findEvent(UUID.fromString(eventId)))) {
//                case Some(event) => complete(event)
//                case None        => complete(NotFound)
//              }
//            }
//          }
//        } ~
//          path("getByUser") {
//            parameters('userId.as[Int]) { userId =>
//              // GET /fp-edu/v1/events/getByUser
//              get {
//                onSuccess(tfc.toFuture(eventsService.findEventsByUser(userId))) { list =>
//                  complete(list)
//                }
//              }
//            }
//          } ~
//          path("list") {
//            parameters('skip.as[Int].?, 'take.as[Int].?) { (skip, take) =>
//              // GET /fp-edu/v1/events/list
//              get {
//                onSuccess(tfc.toFuture(eventsService.listEvents(skip, take))) { list =>
//                  complete(list)
//                }
//              }
//            }
//          } ~
//          path("create") {
//            // POST /fp-edu/v1/events/create
//            put {
//              entity(as[CreateEvent]) { event =>
//                onSuccess(tfc.toFuture(eventsService.createEvent(event))) { response =>
//                  complete(response)
//                }
//              }
//            }
//          }
//      }
//    }
//
//}
