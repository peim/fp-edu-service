package com.peim.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.http.api.{EventsApi, GroupsApi, UsersApi}

class Service() {

  val groupsApi = new GroupsApi
  val eventsApi = new EventsApi
  val usersApi  = new UsersApi

  def routes: Route = pathPrefix("fp-edu") {
    groupsApi.routes ~ eventsApi.routes ~ usersApi.routes
  }

}
