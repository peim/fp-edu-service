package com.peim.clients

import com.peim.config.AppConfig
import com.peim.models.tables.EventEntity
import io.circe.Json
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.client3._
import sttp.client3.circe._
import zio._

object NotifyService {

  type SttpClient = SttpBackend[Task, ZioStreams with WebSockets]

  final case class Service(config: AppConfig, client: SttpClient) {
    def notify(json: Json) = {//: ZIO[SttpClient, Throwable, Response[EventEntity]] = {
      basicRequest
        .post(uri"${config.httpConfig}/notify")
        .body(json)
        .response(asJson[EventEntity])
        .send(client)
        .map(_.body)
    }
  }

  val live: ZLayer[AppConfig with SttpClient, Throwable, NotifyService.Service] = {
    ZLayer {
      for {
        client <- ZIO.service[SttpClient]
        config <- ZIO.service[AppConfig]
        service = Service(config, client)
      } yield service
    }
  }

}
