package com.peim.errors

object WebServiceErrors {

  def badRequest(message: String): ServiceError = {
    new ServiceError(BadRequestCode, message)
  }

  def badRequest(message: String, cause: Throwable): ServiceError = {
    new ServiceError(BadRequestCode, message, cause)
  }

  def notFound(message: String): ServiceError = {
    new ServiceError(NotFoundCode, message)
  }

  def notFound(message: String, cause: Throwable): ServiceError = {
    new ServiceError(NotFoundCode, message, cause)
  }

  def internalError(message: String): ServiceError = {
    new ServiceError(InternalErrorCode, message)
  }

  def internalError(message: String, cause: Throwable): ServiceError = {
    new ServiceError(InternalErrorCode, message, cause)
  }

}
