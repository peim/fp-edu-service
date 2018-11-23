package com.peim.http

import akka.http.scaladsl.marshalling.ToResponseMarshaller

import scala.concurrent.Future

trait ToFutureConversion[F[_]] {

  def toFuture[A: ToResponseMarshaller](a: F[A]): Future[A]

}
