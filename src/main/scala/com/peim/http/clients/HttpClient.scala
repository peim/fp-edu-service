//package com.peim.http.clients
//
//import akka.http.scaladsl.model.StatusCodes._
//import akka.http.scaladsl.model.HttpResponse
//import akka.stream.ActorMaterializer
//import cats.effect.Sync
//import cats.implicits._
//import com.peim.errors.ServiceError
//import com.peim.errors.WebServiceErrors._
//import com.peim.utils.FromFutureConversion
//import io.circe._
//import io.circe.parser._
//
//abstract class HttpClient[F[_]: Sync] {
//
//  implicit def actorMaterializer: ActorMaterializer
//  implicit def fromFutureConversion: FromFutureConversion[F]
//
//  def responseHandler[T](response: HttpResponse)(implicit d: Decoder[T]): F[Either[ServiceError, T]] = {
//    response.status match {
//      case OK =>
//        fromFutureConversion.fromFuture {
//          response.entity.dataBytes.map(_.utf8String).runReduce(_ + _)
//        } map { json =>
//          decode[T](json).left.map(e => internalError(e.getMessage, e.getCause))
//        }
//      case BadRequest => Sync[F].delay(Left(badRequest(response.toString)))
//      case NotFound   => Sync[F].delay(Left(notFound(response.toString)))
//      case _          => Sync[F].delay(Left(internalError(response.toString)))
//    }
//  }
//
//}
