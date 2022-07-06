package com.peim.errors

import cats.syntax.option._
import io.circe.{Decoder, Encoder}
import org.apache.commons.lang3.exception.ExceptionUtils
import sttp.tapir.{Schema, SchemaType}

case class ServiceError(code: ErrorCode, message: String, cause: Option[Throwable])
  extends Exception(message, cause.orNull)

object ServiceError {
  implicit val throwableEncoder: Encoder[Throwable] = Encoder.encodeString.contramap(ExceptionUtils.getStackTrace)
  implicit val throwableDecoder: Decoder[Throwable] = Decoder.decodeString.map(t => new Exception(t))
  implicit val throwableSchema: Schema[Throwable]   = Schema(SchemaType.SString[Throwable]())

  implicit val errorDecoder: Decoder[ServiceError] =
    Decoder.forProduct3("code", "message", "cause")(ServiceError.apply)
  implicit val errorEncoder: Encoder[ServiceError] =
    Encoder.forProduct3("code", "message", "cause")(e => (e.code, e.message, e.cause))
  implicit val schema: Schema[ServiceError] = Schema.derived[ServiceError]

  def from(code: ErrorCode, message: String): ServiceError =
    ServiceError(code, message, None)

  def from(code: ErrorCode, message: String, cause: Throwable): ServiceError =
    ServiceError(code, message, cause.some)
}