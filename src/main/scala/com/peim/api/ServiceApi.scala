package com.peim.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.models.Foo
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.Future

class ServiceApi() {

  def routes: Route = pathPrefix("service") {
    pathPrefix("v1") {
      pathPrefix("status" / Segment) { queryParam =>
        // GET /service/v1/status
        get {
          onSuccess(fooResult(queryParam)) { foo =>
            complete(foo)
          }
        }
      }
    }
  }

  private def fooResult(param: String): Future[Foo] = {
    Future.successful {
      Foo(param.contains("i"), Some(param), param.map(_.toInt))
    }
  }

}
