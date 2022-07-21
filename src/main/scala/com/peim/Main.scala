package com.peim

import cats.syntax.all._
import com.comcast.ip4s._
import com.peim.clients.NotifyService
import com.peim.config.AppConfig
import com.peim.dao.EventsDao
import com.peim.http.api.EventsRoutes
import com.peim.services.EventsService
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger
import sttp.client3.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import zio.interop.catz._
import zio._

object Main extends ZIOApp {
  type AppEnv = EventsService.Service with Clock

  override type Environment = AppEnv with AppConfig

  override implicit def environmentTag: zio.EnvironmentTag[Environment] = EnvironmentTag[Environment]

  override def bootstrap: ZLayer[ZIOAppArgs with Scope, Any, Environment] = ZLayer.make[AppEnv with Scope with AppConfig](
    Scope.default,
    ZLayer.succeed(Clock.ClockLive),
    AppConfig.live,
//    Database.live,
    EventsDao.live,
    AsyncHttpClientZioBackend.layer(),
    NotifyService.live,
    EventsService.live,
  )

  def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any]  = {
    val swaggerRoutes: HttpRoutes[RIO[Environment, *]] = ZHttp4sServerInterpreter()
      .from(
        SwaggerInterpreter().fromServerEndpoints(
          EventsRoutes.endpoints,
          "ZIO demo",
          "0.1.0"
        )
      )
      .toRoutes

    val serviceRoutes: HttpRoutes[RIO[Environment, *]] = ZHttp4sServerInterpreter()
      .from(EventsRoutes.endpoints)
      .toRoutes

    val routes = (serviceRoutes <+> swaggerRoutes).orNotFound

    val httpApp = Logger.httpApp(true, true)(routes)

    EmberServerBuilder.default[RIO[Environment, *]]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApp)
      .build
      .use(_ => ZIO.never)
      .tapError(err => ZIO.logError(err.getMessage))
      .exitCode
  }
}
