lazy val akkaHttpVersion = "10.0.11"
lazy val akkaVersion     = "2.5.11"
lazy val doobieVersion   = "0.6.0"
lazy val circeVersion    = "0.10.0"

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "com.peim",
      scalaVersion := "2.12.7"
    )),
  name := "fp-edu-service",
  scalacOptions ++= Seq(
    "-Ypartial-unification",
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xlint",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"
  ),
  scalafmtOnCompile in ThisBuild := true,
  resolvers += Resolver.bintrayRepo("hseeberger", "maven"),
  libraryDependencies ++= Seq(
    //      https://tpolecat.github.io/doobie/
    "com.typesafe.akka" %% "akka-http"           % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream"         % akkaVersion,
    "de.heikoseeberger" %% "akka-http-circe"     % "1.22.0",
    "io.circe"          %% "circe-core"          % circeVersion,
    "io.circe"          %% "circe-generic"       % circeVersion,
    "io.circe"          %% "circe-parser"        % circeVersion,
    "org.tpolecat"      %% "doobie-core"         % doobieVersion,
    "org.tpolecat"      %% "doobie-h2"           % doobieVersion,
    "org.tpolecat"      %% "doobie-hikari"       % doobieVersion,
    "org.tpolecat"      %% "doobie-postgres"     % doobieVersion,
    "com.typesafe.akka" %% "akka-http-testkit"   % akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-testkit"        % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
    "org.tpolecat"      %% "doobie-specs2"       % doobieVersion % Test,
    "org.tpolecat"      %% "doobie-scalatest"    % doobieVersion % Test,
    "org.scalatest"     %% "scalatest"           % "3.0.1" % Test
  )
)
