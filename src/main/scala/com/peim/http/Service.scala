package com.peim.http

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import cats.effect.{Async, ContextShift}
import com.peim.config.AppConfig
import com.peim.dao.impl.{EventsDaoImpl, GroupsDaoImpl, UsersDaoImpl}
import com.peim.http.api.{EventsApi, GroupsApi, UsersApi}
import com.peim.http.clients.impl.SomeClientImpl
import com.peim.services.{EventsService, GroupsService, UsersService}

class Service[F[_]: Async](config: AppConfig)(implicit cs: ContextShift[F],
                                              tfc: ToFutureConversion[F],
                                              ffc: FromFutureConversion[F],
                                              system: ActorSystem,
                                              materializer: ActorMaterializer) {

  def routes: F[Route] =
    Database
      .createTransactor(config.dbConfig)
      .use { transactor =>
        val someClient = new SomeClientImpl[F]

        val groupsService = new GroupsService[F](new GroupsDaoImpl, someClient, transactor)
        val groupsApi     = new GroupsApi[F](groupsService)

        val eventsService = new EventsService[F](new EventsDaoImpl, transactor)
        val eventsApi     = new EventsApi[F](eventsService)

        val usersService = new UsersService[F](new UsersDaoImpl, transactor)
        val usersApi     = new UsersApi[F](usersService)
        Async[F].pure {
          pathPrefix("fp-edu") {
            groupsApi.routes ~ eventsApi.routes ~ usersApi.routes
          }
        }
      }

}
