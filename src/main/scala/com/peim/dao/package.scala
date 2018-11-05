package com.peim

import java.util.UUID

import cats.syntax.either._
import com.peim.models.{EventType, GroupType, Payload}
import doobie.util.Meta
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Json, parser}

package object dao {

  implicit val uuidMeta: Meta[UUID] = Meta[String].timap(e => UUID.fromString(e))(_.toString)

  implicit val eventTypeMeta: Meta[EventType] = Meta[String].timap(EventType.fromString)(EventType.toString)

  implicit val groupTypeMeta: Meta[GroupType] = Meta[String].timap(GroupType.fromString)(GroupType.toString)

  implicit val eventPayloadMeta: Meta[Payload] = {
    val fromString = (jsonStr: String) =>
      parser.parse(jsonStr).leftMap[Json](err => throw err).merge.as[Payload] match {
        case Right(result) => result
        case Left(error)   => throw error
      }
    val toString = (payload: Payload) => payload.asJson.toString
    Meta[String].timap[Payload](fromString)(toString)
  }

}
