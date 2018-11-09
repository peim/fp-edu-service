package com.peim.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.config.Database
import com.peim.dao.impl.{EventsDaoImpl, GroupsDaoImpl, UsersDaoImpl}
import com.peim.http.api.{EventsApi, GroupsApi, UsersApi}
import com.peim.services.{EventsService, GroupsService, UsersService}
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.ExecutionContext

class Service(implicit ec: ExecutionContext) {

  implicit val s: Scheduler = Scheduler(ec)

  private val transactor = Database.transactor
  private val groupsApi  = new GroupsApi(new GroupsService[Task](new GroupsDaoImpl, transactor))
  private val eventsApi  = new EventsApi(new EventsService[Task](new EventsDaoImpl, transactor))
  private val usersApi   = new UsersApi(new UsersService[Task](new UsersDaoImpl, transactor))

  def routes: Route = pathPrefix("fp-edu") {
    groupsApi.routes ~ eventsApi.routes ~ usersApi.routes
  }

}
