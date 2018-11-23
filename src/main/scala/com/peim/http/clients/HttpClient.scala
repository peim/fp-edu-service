package com.peim.http.clients

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.HttpResponse
import akka.stream.ActorMaterializer
import cats.effect.Async
import cats.implicits._
import com.peim.errors.ServiceError
import com.peim.errors.WebServiceErrors._
import com.peim.http.FromFutureConversion
import io.circe._
import io.circe.parser._

abstract class HttpClient[F[_]: Async] {

  implicit def actorMaterializer: ActorMaterializer
  implicit def fromFutureConversion: FromFutureConversion[F]

  def responseHandler[T](response: HttpResponse)(implicit d: Decoder[T]): F[Either[ServiceError, T]] = {
    response.status match {
      case OK =>
        fromFutureConversion.fromFuture {
          response.entity.dataBytes.map(_.utf8String).runReduce(_ + _)
        } map { json =>
          decode[T](json).left.map(e => internalError(e.getMessage, e.getCause))
        }
      case BadRequest => Async[F].pure(Left(badRequest(response.toString)))
      case NotFound   => Async[F].pure(Left(notFound(response.toString)))
      case _          => Async[F].pure(Left(internalError(response.toString)))
    }
  }

}
