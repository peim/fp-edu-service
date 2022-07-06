package com.peim.models

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

import java.time.Instant

case class Payload(a: String, b: Option[String], c: Boolean, d: Int, e: Instant)

object Payload {
  implicit val codec: Codec[Payload] = deriveCodec
}