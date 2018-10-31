package com.peim.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.Future

class ServiceApi() {

  def routes: Route = pathPrefix("service") {
    pathPrefix("v1") {
      pathPrefix("status" / Segment) { queryParam =>
        // GET /service/v1/status
        get {
          onSuccess(Future.successful(s"hello $queryParam")) { result =>
            complete(OK, result)
          }
        }
      }
    }
  }

}
