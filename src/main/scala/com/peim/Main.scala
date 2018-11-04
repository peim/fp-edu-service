package com.peim

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.peim.api.Service
import com.peim.config.{AppConfig, DatabaseConfig}
import com.typesafe.config.ConfigFactory

import scala.util.{Failure, Success}

object Main extends App {

  val config = new AppConfig

  implicit val system: ActorSystem             = ActorSystem(config.systemName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext                = system.dispatcher

  private val config5 = ConfigFactory.load()

  val log = Logging(system.eventStream, config.systemName)

  try {

    println("!!! " + DatabaseConfig.program.unsafeRunSync)

    lazy val service = new Service()
    Http()
      .bindAndHandle(service.routes, config.httpHost, config.httpPort)
      .map { binding =>
        log.info("REST interface bound to {}", binding.localAddress)
      } transform {
      case s @ Success(_) => s
      case Failure(cause) =>
        log.error(cause, "REST interface could not bind to {}:{}", config.httpHost, config.httpPort)
        Failure(cause)
    }
  } catch {
    case e: Throwable =>
      log.error(e, e.getMessage)
      system.terminate()
  }

}
