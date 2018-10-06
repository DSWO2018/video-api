name := """video-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.6")

libraryDependencies += guice
libraryDependencies += jdbc

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.mariadb.jdbc" % "mariadb-java-client" % "2.3.0"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.0"