package com.peim.http

import scala.concurrent.Future

trait FromFutureConversion[F[_]] {

  def fromFuture[A](a: Future[A]): F[A]

}
