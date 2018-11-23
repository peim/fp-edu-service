package com.peim.errors

case class ServiceError(
    code: ErrorCode,
    message: String,
    cause: Option[Throwable]
) extends RuntimeException(s"[$code] $message", cause.orNull) {

  def this(code: ErrorCode, message: String) = this(code, message, None)
  def this(code: ErrorCode, message: String, cause: Throwable) = this(code, message, Some(cause))

  override def toString: String = {
    cause.foldLeft(
      s"[$code] $message"
    )((result, cause) => result + s", cause ${cause.getMessage}")
  }

}
