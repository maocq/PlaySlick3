name := """PlaySlick3"""
organization := "com.maocq"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies ++= Seq(
  evolutions,
  "com.typesafe.play"               %% "play-slick"              % "3.0.0",
  "com.typesafe.play"               %% "play-slick-evolutions"   % "3.0.0",
  "org.postgresql"                  % "postgresql"               % "42.1.4",
  "org.scalatestplus.play"          %% "scalatestplus-play"      % "3.1.2" % Test
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.maocq.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.maocq.binders._"
