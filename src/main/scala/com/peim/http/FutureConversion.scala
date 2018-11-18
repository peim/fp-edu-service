package com.peim.http

import akka.http.scaladsl.marshalling.ToResponseMarshaller

import scala.concurrent.Future

trait FutureConversion[F[_]] {
  def toFuture[A: ToResponseMarshaller](a: F[A]): Future[A]
}
