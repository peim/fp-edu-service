package com.peim

import cats.syntax.all._
import com.comcast.ip4s._
import com.peim.config.AppConfig
import com.peim.dao.EventsDao
import com.peim.http.api.EventsRoutes
import com.peim.services.EventsService
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import zio.interop.catz._
import zio.{Clock, EnvironmentTag, ExitCode, RIO, ZEnv, ZIO, ZIOApp, ZIOAppArgs, ZLayer}

object Main extends ZIOApp {
  override implicit def tag: zio.EnvironmentTag[Environment] = EnvironmentTag[Environment]

  type AppEnv = EventsService.Service with Clock

  override type Environment = AppEnv with AppConfig with ZEnv

  override def layer: ZLayer[ZIOAppArgs, Any, Environment] = ZLayer.make[AppEnv with AppConfig with ZEnv](
    ZEnv.live,
    AppConfig.live,
    EventsDao.live,
    EventsService.live,
  )

  override def run: ZIO[AppEnv with AppConfig, Any, ExitCode] = {
     for {
      config <- ZIO.service[AppConfig]
      _ = println(config.httpConfig)

      swaggerRoutes = ZHttp4sServerInterpreter()
        .from(
          SwaggerInterpreter().fromServerEndpoints(
            EventsRoutes.endpoints,
            "Monarch",
            "0.1.0"
          )
        )
        .toRoutes

      serviceRoutes = ZHttp4sServerInterpreter()
        .from(EventsRoutes.endpoints)
        .toRoutes

      routes = (serviceRoutes <+> swaggerRoutes).orNotFound

      httpApp = Logger.httpApp(true, true)(routes)

      server <- EmberServerBuilder.default[RIO[AppEnv, *]]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8081")
        .withHttpApp(httpApp)
        .build
        .use(_ => ZIO.never)
        .tapError(err => ZIO.logError(err.getMessage))
        .exitCode
    } yield server
  }

}
