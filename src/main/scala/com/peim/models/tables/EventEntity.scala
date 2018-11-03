package com.peim.models.tables

import java.time.Instant
import java.util.UUID

import com.peim.models.{EventType, Payload}

case class EventEntity(
    id: UUID,
    payload: Payload,
    userId: Int,
    `type`: EventType,
    created: Instant,
    source: Option[String]
)
