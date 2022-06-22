package com.peim

import zio.{ZEnv, ZIO, ZIOApp, ZIOAppArgs, ZLayer}

object Main extends ZIOApp {
  override implicit def tag: zio.EnvironmentTag[Main.type] = ???

  override type Environment = this.type

  override def layer: ZLayer[ZIOAppArgs, Any, Main.type] = ???

  override def run: ZIO[Main.type with ZEnv with ZIOAppArgs, Any, Any] = ???
}
