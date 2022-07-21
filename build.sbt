val Http4sVersion = "0.23.12"
val CirceVersion = "0.14.2"
val TapirVersion = "1.0.2"
val SttpClientVersion = "3.7.0"
val DoobieVersion = "1.0.0-RC2"
val MunitVersion = "0.7.29"
val MunitCatsEffectVersion = "1.0.7"
val LogbackVersion = "1.2.10"

lazy val root = (project in file("."))
  .enablePlugins(ScalafmtPlugin)
  .settings(
    organization := "com.example",
    name := "fp-edu-service",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.8",
    ThisBuild / useCoursier := true,
    ThisBuild / scalafmtOnCompile := true,
    Test / parallelExecution := true,
    run / fork := true,
    libraryDependencies ++= Seq(
      "org.http4s"                  %% "http4s-ember-server"     % Http4sVersion,
      "org.http4s"                  %% "http4s-circe"            % Http4sVersion,
      "org.http4s"                  %% "http4s-dsl"              % Http4sVersion,
      "io.circe"                    %% "circe-generic"           % CirceVersion,
      "io.circe"                    %% "circe-generic-extras"    % CirceVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"     % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-core"              % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"        % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server-zio" % TapirVersion,

      "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % SttpClientVersion,
      "com.softwaremill.sttp.client3" %% "slf4j-backend"                 % SttpClientVersion,
      "com.softwaremill.sttp.client3" %% "circe"                         % SttpClientVersion,
      "com.softwaremill.sttp.client3" %% "core"                         % SttpClientVersion,
      "com.softwaremill.sttp.client3" %% "zio"                         % SttpClientVersion,


      "dev.zio" %% "zio-interop-cats" % "3.3.0",

      "org.tpolecat" %% "doobie-core"      % DoobieVersion,
      "org.tpolecat" %% "doobie-h2"        % DoobieVersion,
      "org.tpolecat" %% "doobie-hikari"    % DoobieVersion,
//      "org.tpolecat" %% "doobie-postgres"  % DoobieVersion,
      "org.tpolecat" %% "doobie-specs2"    % DoobieVersion % Test,
      "org.tpolecat" %% "doobie-scalatest" % DoobieVersion % Test,


      "com.github.pureconfig"       %% "pureconfig"              % "0.17.1",
      "org.apache.commons"          % "commons-lang3"            % "3.12.0",
      "org.scalameta"               %% "munit"                   % MunitVersion           % Test,
      "org.typelevel"               %% "munit-cats-effect-3"     % MunitCatsEffectVersion % Test,
      "ch.qos.logback"              %  "logback-classic"         % LogbackVersion         % Runtime,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )
