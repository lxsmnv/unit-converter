name := "unit-converter"

version := "0.1"

scalaVersion := "2.13.5"


libraryDependencies += "org.typelevel" %% "cats-core" % "2.4.1"
libraryDependencies += "org.typelevel" %% "cats-effect" % "2.4.0"
libraryDependencies += "co.fs2" %% "fs2-core" % "2.5.1"
libraryDependencies += "io.circe" %% "circe-core" % "0.13.0"
libraryDependencies += "io.circe" %% "circe-literal" % "0.13.0"
libraryDependencies += "io.circe" %% "circe-generic" % "0.13.0"
libraryDependencies += "org.http4s" %% "http4s-circe" % "0.21.6"
libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.21.6"
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "0.21.6"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
libraryDependencies += "org.specs2" %% "specs2-core" % "4.10.6" % Test


scalacOptions ++= Seq("-deprecation")
