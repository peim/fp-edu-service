package com.peim.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.effect.{Async, ContextShift}
import com.peim.config.AppConfig
import com.peim.dao.impl.{EventsDaoImpl, GroupsDaoImpl, UsersDaoImpl}
import com.peim.http.api.GroupsApi
import com.peim.services.{EventsService, GroupsService, UsersService}

class Service[F[_]: Async](config: AppConfig)(implicit cs: ContextShift[F], fc: FutureConversion[F]) {

  def routes: F[Route] =
    Database
      .createTransactor(config.dbConfig)
      .use { transactor =>
        val groupsApi = new GroupsApi(new GroupsService[F](new GroupsDaoImpl, transactor))
        //        val eventsApi = new EventsApi(new EventsService[F](new EventsDaoImpl, transactor))
        //        val usersApi  = new UsersApi(new UsersService[F](new UsersDaoImpl, transactor))

        Async[F].pure {
          pathPrefix("fp-edu") {
            groupsApi.routes //~ eventsApi.routes ~ usersApi.routes
          }
        }
      }

}
