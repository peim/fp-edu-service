package com.peim.errors

import com.peim.errors.ErrorCode._


object ServiceErrors {

  def badRequest(message: String): ServiceError =
    ServiceError.from(BadRequestCode, message)

  def badRequest(message: String, cause: Throwable): ServiceError =
    ServiceError.from(BadRequestCode, message, cause)

  def internalError(message: String): ServiceError =
    ServiceError.from(InternalErrorCode, message)

  def internalError(message: String, cause: Throwable): ServiceError =
    ServiceError.from(InternalErrorCode, message, cause)
}

