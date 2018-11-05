package com.peim.http.api

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.models.api.in.CreateEvent
import com.peim.services.EventsService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

class EventsApi(eventsService: EventsService) {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("events") {
        pathPrefix("get") {
          pathEnd {
            parameters('id.as[String]) { eventId =>
              // GET /fp-edu/v1/events/get
              get {
                onSuccess(eventsService.findEvent(UUID.fromString(eventId))) {
                  case Some(event) => complete(event)
                  case None        => complete(NotFound)
                }
              }
            }
          }
        } ~
          pathPrefix("getByUser") {
            pathEnd {
              parameters('userId.as[Int]) { userId =>
                // GET /fp-edu/v1/events/getByUser
                get {
                  onSuccess(eventsService.findEventsByUser(userId)) { list =>
                    complete(list)
                  }
                }
              }
            }
          } ~
          pathPrefix("list") {
            pathEnd {
              parameters('skip.as[Int].?, 'take.as[Int].?) { (skip, take) =>
                // GET /fp-edu/v1/events/list
                get {
                  onSuccess(eventsService.listEvents(skip, take)) { list =>
                    complete(list)
                  }
                }
              }
            }
          } ~
          pathPrefix("create") {
            pathEndOrSingleSlash {
              // POST /fp-edu/v1/events/create
              put {
                entity(as[CreateEvent]) { event =>
                  onSuccess(eventsService.createEvent(event)) { response =>
                    complete(response)
                  }
                }
              }
            }
          }
      }
    }

}
