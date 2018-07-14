lazy val akkaHttpVersion = "10.0.11"
lazy val akkaVersion = "2.5.11"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "gr.trapatsas.letshout",
      scalaVersion := "2.12.6"
    )),
    name := "letShout",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-caching" % akkaHttpVersion,

      "com.danielasfregola" %% "twitter4s" % "5.5",
      "com.typesafe" % "config" % "1.3.2",

      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    ),
    resolvers += Resolver.sonatypeRepo("releases")
  )
