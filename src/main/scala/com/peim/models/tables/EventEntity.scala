package com.peim.models.tables

import java.time.Instant
import com.peim.models.{EventType, Payload}
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

final case class EventEntity(
    id: String,
    payload: Payload,
    userId: Int,
    `type`: EventType,
    created: Instant,
    source: Option[String]
)

object EventEntity {
  implicit val codec: Codec[EventEntity] = deriveCodec
}