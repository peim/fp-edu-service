package com.peim.errors

import cats.syntax.either._
import io.circe.{Decoder, Encoder}
import sttp.tapir.{Schema, SchemaType}

sealed trait ErrorCode

object ErrorCode {
  case object BadRequestCode    extends ErrorCode
  case object InternalErrorCode extends ErrorCode

  implicit val schema: Schema[ErrorCode]   = Schema(SchemaType.SString[ErrorCode]())
  implicit val encoder: Encoder[ErrorCode] = Encoder.encodeString.contramap(ErrorCode.toString)
  implicit val decoder: Decoder[ErrorCode] = Decoder.decodeString.emap(ErrorCode.fromString)

  def fromString(s: String): Either[String, ErrorCode] =
    s match {
      case "badRequest"          => BadRequestCode.asRight
      case "internalServerError" => InternalErrorCode.asRight
      case other                 => s"unexpected group type $other".asLeft
    }

  def toString(code: ErrorCode): String = code match {
    case BadRequestCode    => "badRequest"
    case InternalErrorCode => "internalServerError"
  }
}
