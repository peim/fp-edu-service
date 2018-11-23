package com.peim

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import cats.effect.{ContextShift, ExitCode}
import com.peim.config.AppConfig
import com.peim.http.{FromFutureConversion, Service, ToFutureConversion}
import monix.eval.{Task, TaskApp}
import monix.execution.Scheduler

import scala.concurrent.{ExecutionContext, Future}

object Main extends TaskApp {

  implicit val system: ActorSystem             = ActorSystem("fd-edu-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val s: Scheduler                    = Scheduler(system.dispatcher)

  implicit val contextShift: ContextShift[Task] =
    new ContextShift[Task] {
      override def shift: Task[Unit] =
        Task.shift(s)
      override def evalOn[A](ec: ExecutionContext)(fa: Task[A]): Task[A] =
        // !!!!!
        Task.shift(ExecutionContext.Implicits.global).bracket(_ => fa)(_ => Task.shift(s))
      // !!!!!
    }

  implicit object taskToFuture extends ToFutureConversion[Task] {
    override def toFuture[A: ToResponseMarshaller](a: Task[A]): Future[A] = a.runToFuture
  }

  implicit object taskFromFuture extends FromFutureConversion[Task] {
    override def fromFuture[A](a: Future[A]): Task[A] = Task.deferFuture(a)
  }

  override protected val scheduler: Scheduler = s

  override def run(args: List[String]): Task[ExitCode] = {
    val config  = new AppConfig
    val log     = Logging(system.eventStream, config.systemName)
    val service = new Service[Task](config)

    def bindRoutes(routes: Route) =
      Task
        .deferFuture(Http().bindAndHandle(routes, config.httpHost, config.httpPort))
        .attempt
        .flatMap {
          case Right(_) =>
            log.info("REST interface bound to {}:{}", config.httpHost, config.httpPort)
            Task.never
          case Left(cause) =>
            log.error(cause, "REST interface could not bind to {}:{}", config.httpHost, config.httpPort)
            Task.eval(ExitCode.Error)
        }

    def terminateSystem(routes: Route) = Task.fromFuture {
      system.terminate().flatMap(_ => Future.unit)
    }

    service.routes.bracket(bindRoutes)(terminateSystem)
  }

}
