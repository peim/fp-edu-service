package com.peim.http.clients

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.HttpResponse
import akka.stream.ActorMaterializer
import com.peim.errors.ServiceError
import com.peim.errors.WebServiceErrors._
import io.circe._
import io.circe.parser._
import monix.eval.Task

trait HttpClient {

  implicit def actorMaterializer: ActorMaterializer

  def responseHandler[T](response: HttpResponse)(implicit d: Decoder[T]): Task[Either[ServiceError, T]] = {
    response.status match {
      case OK =>
        Task.deferFuture {
          response.entity.dataBytes.map(_.utf8String).runReduce(_ + _)
        } map { json =>
          decode[T](json).left.map(e => internalError(e.getMessage, e.getCause))
        }
      case BadRequest => Task.eval(Left(badRequest(response.toString)))
      case NotFound   => Task.eval(Left(notFound(response.toString)))
      case _          => Task.eval(Left(internalError(response.toString)))
    }
  }

}
