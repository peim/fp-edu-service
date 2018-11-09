package com.peim.http.api

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.models.api.in.CreateEvent
import com.peim.services.EventsService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import monix.eval.Task
import monix.execution.Scheduler

class EventsApi(eventsService: EventsService[Task])(implicit s: Scheduler) {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("events") {
        path("get") {
          parameters('id.as[String]) { eventId =>
            // GET /fp-edu/v1/events/get
            get {
              onSuccess(eventsService.findEvent(UUID.fromString(eventId)).runAsync) {
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
                onSuccess(eventsService.findEventsByUser(userId).runAsync) { list =>
                  complete(list)
                }
              }
            }
          } ~
          path("list") {
            parameters('skip.as[Int].?, 'take.as[Int].?) { (skip, take) =>
              // GET /fp-edu/v1/events/list
              get {
                onSuccess(eventsService.listEvents(skip, take).runAsync) { list =>
                  complete(list)
                }
              }
            }
          } ~
          path("create") {
            // POST /fp-edu/v1/events/create
            put {
              entity(as[CreateEvent]) { event =>
                onSuccess(eventsService.createEvent(event).runAsync) { response =>
                  complete(response)
                }
              }
            }
          }
      }
    }

}
