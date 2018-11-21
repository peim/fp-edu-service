package com.peim.http.api

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.effect.Async
import com.peim.http.FutureConversion
import com.peim.models.api.in.CreateEvent
import com.peim.services.EventsService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

class EventsApi[F[_]: Async](eventsService: EventsService[F])(implicit fc: FutureConversion[F]) {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("events") {
        path("get") {
          parameters('id.as[String]) { eventId =>
            // GET /fp-edu/v1/events/get
            get {
              onSuccess(fc.toFuture(eventsService.findEvent(UUID.fromString(eventId)))) {
                case Some(event) => complete(event)
                case None        => complete(NotFound)
              }
            }
          }
        } ~
          path("getByUser") {
            parameters('userId.as[Int]) { userId =>
              // GET /fp-edu/v1/events/getByUser
              get {
                onSuccess(fc.toFuture(eventsService.findEventsByUser(userId))) { list =>
                  complete(list)
                }
              }
            }
          } ~
          path("list") {
            parameters('skip.as[Int].?, 'take.as[Int].?) { (skip, take) =>
              // GET /fp-edu/v1/events/list
              get {
                onSuccess(fc.toFuture(eventsService.listEvents(skip, take))) { list =>
                  complete(list)
                }
              }
            }
          } ~
          path("create") {
            // POST /fp-edu/v1/events/create
            put {
              entity(as[CreateEvent]) { event =>
                onSuccess(fc.toFuture(eventsService.createEvent(event))) { response =>
                  complete(response)
                }
              }
            }
          }
      }
    }

}
