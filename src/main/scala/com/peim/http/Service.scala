package com.peim.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.config.Database
import com.peim.dao.impl.GroupsDaoImpl
import com.peim.http.api.{EventsApi, GroupsApi, UsersApi}
import com.peim.services.GroupsService

class Service() {

  private val transactor = Database.transactor
  private val groupsApi  = new GroupsApi(new GroupsService(new GroupsDaoImpl, transactor))
  private val eventsApi  = new EventsApi
  private val usersApi   = new UsersApi

  def routes: Route = pathPrefix("fp-edu") {
    groupsApi.routes ~ eventsApi.routes ~ usersApi.routes
  }

}
