name := "scheduler"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies += "io.javalin" % "javalin" % "2.0.0"
libraryDependencies += "com.github.scribejava" % "scribejava-apis" % "3.2.0"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.6"
libraryDependencies += "net.debasishg" %% "redisclient" % "3.7"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.0"
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.2"
)

