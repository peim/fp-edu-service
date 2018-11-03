package com.peim.models.request

import com.peim.models.{EventType, Payload}

case class CreateEvent(
    payload: Payload,
    userId: Int,
    `type`: EventType,
    source: Option[String]
)
