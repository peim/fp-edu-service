package com.peim.http.api

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.models.api.in.CreateEvent
import com.peim.models.tables.EventEntity
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.Future

class EventsApi() {

  def routes: Route =
    pathPrefix("v1") {
      pathPrefix("events") {
        pathPrefix("get") {
          parameters('id.as[String]) { eventId =>
            // GET /fp-edu/v1/events/get
            get {
              onSuccess(findEvent(UUID.fromString(eventId))) {
                case Some(event) => complete(event)
                case None        => complete(NotFound)
              }
            }
          }
        } ~
          pathPrefix("getByUser") {
            parameters('userId.as[Int]) { userId =>
              // GET /fp-edu/v1/events/getByUser
              get {
                onSuccess(findEventsByUser(userId)) { list =>
                  complete(list)
                }
              }
            }
          } ~
          pathPrefix("list") {
            parameters('skip.as[Int].?, 'take.as[Int].?) { (skip, take) =>
              // GET /fp-edu/v1/events/list
              get {
                onSuccess(listEvents(skip, take)) { list =>
                  complete(list)
                }
              }
            }
          } ~
          pathPrefix("create") {
            pathEndOrSingleSlash {
              // POST /fp-edu/v1/events/create
              post {
                entity(as[CreateEvent]) { event =>
                  onSuccess(createEvent(event)) { response =>
                    complete(response)
                  }
                }
              }
            }
          }
      }
    }

  private def findEvent(eventId: UUID): Future[Option[EventEntity]]                            = ???
  private def findEventsByUser(userId: Int): Future[Seq[EventEntity]]                          = ???
  private def listEvents(takeOpt: Option[Int], skipOpt: Option[Int]): Future[Seq[EventEntity]] = ???
  private def createEvent(group: CreateEvent): Future[Option[UUID]]                            = ???

}
