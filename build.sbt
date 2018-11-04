import Dependencies._

lazy val root = (project in file("."))
  .settings(commonSettings)
  .enablePlugins(ScalafmtPlugin)

lazy val commonSettings = Seq(
  organization := "com.peim",
  scalaVersion := "2.12.7",
  version := "1.0",
  name := "fp-edu-service",
  scalafmtOnCompile in ThisBuild := true,
  resolvers ++= projectResolvers,
  libraryDependencies ++= dependencies,
  scalacOptions ++= compileSettings
)
