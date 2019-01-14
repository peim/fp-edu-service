package com.peim.http.clients.impl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethod, HttpRequest}
import akka.stream.ActorMaterializer
import cats.effect.Sync
import cats.implicits._
import com.peim.errors.ServiceError
import com.peim.http.clients.{HttpClient, SomeClient}
import com.peim.models.GroupsTree
import com.peim.utils.FromFutureConversion
import io.circe.generic.auto._

class SomeClientImpl[F[_]: Sync](implicit ffc: FromFutureConversion[F],
                                 system: ActorSystem,
                                 materializer: ActorMaterializer)
    extends HttpClient[F]
    with SomeClient[F] {

  implicit def actorMaterializer: ActorMaterializer          = materializer
  implicit def fromFutureConversion: FromFutureConversion[F] = ffc

  override def getSmth(id: String): F[Either[ServiceError, GroupsTree]] = {
    ffc.fromFuture {
      val request = HttpRequest()
        .withUri("http://localhost:8080/proposal/run")
        .withMethod(HttpMethod.custom("POST"))
      Http().singleRequest(request)
    } flatMap {
      responseHandler[GroupsTree]
    }
  }

}
