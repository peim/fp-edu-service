package com.peim.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.config.Database
import com.peim.dao.impl.{EventsDaoImpl, GroupsDaoImpl, UsersDaoImpl}
import com.peim.http.api.{EventsApi, GroupsApi, UsersApi}
import com.peim.services.{EventsService, GroupsService, UsersService}

import scala.concurrent.ExecutionContext

class Service(implicit ec: ExecutionContext) {

  private val transactor = Database.transactor
  private val groupsApi  = new GroupsApi(new GroupsService(new GroupsDaoImpl, transactor))
  private val eventsApi  = new EventsApi(new EventsService(new EventsDaoImpl, transactor))
  private val usersApi   = new UsersApi(new UsersService(new UsersDaoImpl, transactor))

  def routes: Route = pathPrefix("fp-edu") {
    groupsApi.routes ~ eventsApi.routes ~ usersApi.routes
  }

}
