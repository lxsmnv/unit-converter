
name := "unit-converter"

version := "0.1"

scalaVersion := "2.13.5"


libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.30"
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.30"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.4.1"
libraryDependencies += "org.typelevel" %% "cats-effect" % "2.4.1"
libraryDependencies += "io.circe" %% "circe-core" % "0.13.0"
libraryDependencies += "io.circe" %% "circe-literal" % "0.13.0"
libraryDependencies += "io.circe" %% "circe-generic" % "0.13.0"
libraryDependencies += "io.circe" %% "circe-generic-extras" % "0.13.0"
libraryDependencies += "org.http4s" %% "http4s-circe" % "0.21.6"
libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.21.6"
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.21.6"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
libraryDependencies += "org.specs2" %% "specs2-cats" % "4.9.4" % Test
libraryDependencies += "org.specs2" %% "specs2-core" % "4.10.6" % Test
libraryDependencies += "org.specs2" %% "specs2-http4s" % "1.0.0" % Test

dockerExposedPorts := Seq(8080)
scalacOptions ++= Seq("-deprecation", "-Ymacro-annotations")

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)