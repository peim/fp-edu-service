package com.peim.errors

import io.circe.{Decoder, Encoder}

sealed trait ErrorCode
case object BadRequestCode    extends ErrorCode
case object NotFoundCode      extends ErrorCode
case object InternalErrorCode extends ErrorCode

object ErrorCode {

  implicit val errorCodeEncoder: Encoder[ErrorCode] = Encoder.encodeString.contramap(toString)
  implicit val errorCodeDecoder: Decoder[ErrorCode] = Decoder.decodeString.map(fromString)

  def fromString(s: String): ErrorCode =
    s match {
      case "badRequest"          => BadRequestCode
      case "notFound"            => NotFoundCode
      case "internalServerError" => InternalErrorCode
      case other                 => throw new RuntimeException(s"unexpected group type $other")
    }

  def toString(code: ErrorCode): String = code match {
    case BadRequestCode    => "badRequest"
    case NotFoundCode      => "notFound"
    case InternalErrorCode => "internalServerError"
  }

}
