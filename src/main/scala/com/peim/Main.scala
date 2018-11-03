package com.peim

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.peim.api.ServiceApi
import com.peim.config.DatabaseConfig
import com.typesafe.config.ConfigFactory

import scala.util.{Failure, Success}

object Main extends App {

  val systemName = "fp-edu-service"

  implicit val system: ActorSystem             = ActorSystem(systemName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext                = system.dispatcher

  val log = Logging(system.eventStream, systemName)

  try {
    val config           = ConfigFactory.load()
    val httpHost: String = config.getString("http.host")

    val httpPort: Int = config.getInt("http.port")

//    println("!!! " + DatabaseConfig.program.unsafeRunSync)

    lazy val service = new ServiceApi()
    Http()
      .bindAndHandle(service.routes, httpHost, httpPort)
      .map { binding =>
        log.info("REST interface bound to {}", binding.localAddress)
      } transform {
      case s @ Success(_) => s
      case Failure(cause) =>
        log.error(cause, "REST interface could not bind to {}:{}", httpHost, httpPort)
        Failure(cause)
    }
  } catch {
    case e: Throwable =>
      log.error(e, e.getMessage)
      system.terminate()
  }

}
