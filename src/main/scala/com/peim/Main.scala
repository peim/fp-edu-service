package com.peim

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.peim.api.ServiceApi
import com.typesafe.config.ConfigFactory

object Main extends App {

  val systemName = "fp-edu-service"

  implicit val system: ActorSystem = ActorSystem(systemName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val log = Logging(system.eventStream, systemName)

  try {
    val config = ConfigFactory.load()
    val httpHost: String = config.getString("http.host")

    val httpPort: Int = config.getInt("http.port")

    lazy val service = new ServiceApi()
    Http().bindAndHandle(service.routes, httpHost, httpPort).map {
      binding => log.info("REST interface bound to {}", binding.localAddress)
    }.onFailure {
      case ex: Exception =>
        log.error(ex, "REST interface could not bind to {}:{}", httpHost, httpPort)
        system.terminate()
    }
  } catch {
    case e: Throwable =>
      log.error(e, e.getMessage)
      system.terminate()
  }

}
