import sbt._

object Dependencies {

  val akkaHttpVersion = "10.0.11"
  val akkaVersion     = "2.5.11"
  val doobieVersion   = "0.6.0"
  val circeVersion    = "0.10.0"
  val monixVersion    = "3.0.0-RC1"

  lazy val projectResolvers = Seq(
    Resolver.bintrayRepo("hseeberger", "maven")
  )

  lazy val dependencies = testDependencies ++ rootDependencies

  lazy val rootDependencies = Seq(
    "ch.qos.logback"    % "logback-classic"      % "1.2.3",
    "com.typesafe.akka" %% "akka-http"           % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream"         % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j"          % akkaVersion,
    "de.heikoseeberger" %% "akka-http-circe"     % "1.22.0",
    "io.monix"          %% "monix"               % monixVersion,
    "io.circe"          %% "circe-core"          % circeVersion,
    "io.circe"          %% "circe-generic"       % circeVersion,
    "io.circe"          %% "circe-parser"        % circeVersion,
    "org.tpolecat"      %% "doobie-core"         % doobieVersion,
    "org.tpolecat"      %% "doobie-h2"           % doobieVersion,
    "org.tpolecat"      %% "doobie-hikari"       % doobieVersion,
    "org.tpolecat"      %% "doobie-postgres"     % doobieVersion,
  )

  lazy val testDependencies = Seq (
    "com.typesafe.akka" %% "akka-http-testkit"   % akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-testkit"        % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
    "org.tpolecat"      %% "doobie-specs2"       % doobieVersion % Test,
    "org.tpolecat"      %% "doobie-scalatest"    % doobieVersion % Test,
    "org.scalatest"     %% "scalatest"           % "3.0.1" % Test
  )

  lazy val compileSettings = Seq(
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
  )

}
