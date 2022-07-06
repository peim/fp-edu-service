package com.peim.http.api

import com.peim.errors.{ServiceError, ServiceErrors}
import com.peim.models.api.in.CreateEvent
import com.peim.models.tables.EventEntity
import com.peim.services.EventsService
import io.circe.generic.auto._
import sttp.tapir.{EndpointInput, endpoint}
import sttp.tapir.ztapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.ztapir.ZServerEndpoint

object EventsRoutes {

  import sttp.tapir.EndpointIO.annotations ._

  @endpointInput("?id=")
  case class EventId(
                   @path
                   id: String
                 )

  @endpointInput("?id=")
  case class UserId(
                      @path
                      id: Int
                    )

  @endpointInput("?take=")
  case class Take(
                     @path
                     take: Int
                   )

  @endpointInput("?skip=")
  case class Skip(
                   @path
                   skip: Int
                 )

  private val eventIdParam: EndpointInput[EventId] = path[String]("id").mapTo[EventId]
  private val userIdParam: EndpointInput[UserId] = path[Int]("id").mapTo[UserId]
  private val takeParam: EndpointInput[Take] = path[Int]("take").mapTo[Take]
  private val skipParam: EndpointInput[Skip] = path[Int]("skip").mapTo[Skip]

  private val getEventByIdEndpoint = endpoint.get
    .in("events" / "get")
    .in(eventIdParam)
    .out(jsonBody[EventEntity])
    .errorOut(jsonBody[ServiceError])
    .zServerLogic(id => EventsService.findEvent(id.id).mapError(err => ServiceErrors.internalError(err.getMessage)))

  private val getEventsByUserEndpoint = endpoint.get
    .in("events" / "getByUser")
    .in(userIdParam)
    .out(jsonBody[List[EventEntity]])
    .errorOut(jsonBody[ServiceError])
    .zServerLogic(userId => EventsService.findEventByUser(userId.id).mapError(err => ServiceErrors.internalError(err.getMessage)))

  private val listEventsEndpoint = endpoint.get
    .in("events" / "list")
    .in(takeParam)
    .in(skipParam)
    .out(jsonBody[List[EventEntity]])
    .errorOut(jsonBody[ServiceError])
    .zServerLogic(params => EventsService.listEvents(params._1.take, params._2.skip).mapError(err => ServiceErrors.internalError(err.getMessage)))

  private val createEventEndpoint = endpoint.post
    .in("events" / "create")
    .in(jsonBody[CreateEvent])
    .out(jsonBody[String])
    .errorOut(jsonBody[ServiceError])
    .zServerLogic(event => EventsService.createEvent(event).mapError(err => ServiceErrors.internalError(err.getMessage)))

  val endpoints: List[ZServerEndpoint[EventsService.Service, Any]] = List(
    getEventByIdEndpoint,
    getEventsByUserEndpoint,
    listEventsEndpoint,
    createEventEndpoint
  )

//  val routes =
//    ZHttp4sServerInterpreter()
//      .from(
//        endpoints
//      )
//      .toRoutes


}
