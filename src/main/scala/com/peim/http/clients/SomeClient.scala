package com.peim.http.clients

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpMethod, HttpRequest}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.peim.models.GroupsTree
import monix.eval.Task
import io.circe.generic.auto._

class SomeClient(implicit system: ActorSystem, materializer: ActorMaterializer) extends HttpClient {

  implicit def actorMaterializer: ActorMaterializer = materializer

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

}
