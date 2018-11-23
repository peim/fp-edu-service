package com.peim.utils

import akka.http.scaladsl.marshalling.ToResponseMarshaller

import scala.concurrent.Future


trait FromFutureConversion[F[_]] {

  def fromFuture[A](a: Future[A]): F[A]

}

trait ToFutureConversion[F[_]] {

  def toFuture[A: ToResponseMarshaller](a: F[A]): Future[A]

}

trait FutureConversion[F[_]] extends FromFutureConversion[F] with ToFutureConversion[F]
