package com.peim.http.clients

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpMethod, HttpRequest, HttpResponse}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.peim.errors.ServiceError
import com.peim.errors.WebServiceErrors._
import com.peim.models.GroupsTree
import monix.eval.Task
import io.circe._
import io.circe.parser._
import io.circe.generic.auto._

class SomeClient(implicit system: ActorSystem, materializer: ActorMaterializer) {

  def getSmth(id: String) = {
    Task.deferFuture {
      val request = HttpRequest()
        .withUri("http://localhost:8080/proposal/run")
        .withMethod(HttpMethod.custom("POST"))
      Http().singleRequest(request)
    } flatMap {
      responseHandler[GroupsTree]
    }
  }

  private def responseHandler[T](response: HttpResponse)(implicit d: Decoder[T]): Task[Either[ServiceError, T]] = {
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
