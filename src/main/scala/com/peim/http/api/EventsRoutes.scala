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

  case class Paging(take: Int, skip: Int)
  object Paging {
    def of(takeOpt: Option[Int], skipOpt: Option[Int]): Paging =
      Paging(takeOpt.getOrElse(25), skipOpt.getOrElse(0))
  }

  private val eventIdParam = query[String]("id")
  private val userIdParam = query[Int]("id")
  private val paging: EndpointInput[Paging] =
    query[Option[Int]]("take").and(query[Option[Int]]("skip"))
      .map(input => Paging.of(input._1, input._2))(paging => (Some(paging.take), Some(paging.skip)))

  private val getEventByIdEndpoint = endpoint.get
    .in("events" / "get")
    .in(eventIdParam)
    .out(jsonBody[EventEntity])
    .errorOut(jsonBody[ServiceError])
    .zServerLogic(id => EventsService.findEvent(id).mapError(err => ServiceErrors.internalError(err.getMessage)))

  private val getEventsByUserEndpoint = endpoint.get
    .in("events" / "getByUser")
    .in(userIdParam)
    .out(jsonBody[List[EventEntity]])
    .errorOut(jsonBody[ServiceError])
    .zServerLogic(userId => EventsService.findEventByUser(userId).mapError(err => ServiceErrors.internalError(err.getMessage)))

  private val listEventsEndpoint = endpoint.get
    .in("events" / "list")
    .in(paging)
    .out(jsonBody[List[EventEntity]])
    .errorOut(jsonBody[ServiceError])
    .zServerLogic(paging => EventsService.listEvents(paging.take, paging.skip).mapError(err => ServiceErrors.internalError(err.getMessage)))

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
}
