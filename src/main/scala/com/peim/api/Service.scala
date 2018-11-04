package com.peim.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class Service() {

  val groupsApi = new GroupsApi
  val eventsApi = new EventsApi
  val usersApi  = new UsersApi

  def routes: Route = pathPrefix("fp-edu") {
    groupsApi.routes ~ eventsApi.routes ~ usersApi.routes
  }

}
