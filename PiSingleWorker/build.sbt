name := "PiSingleworker"

version := "1.0"

scalaVersion := "2.10.3"

sbtVersion := "0.13.1"


resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
       "com.typesafe.akka" %% "akka-actor"   % "2.3.2",
       "com.typesafe.akka" %% "akka-testkit" % "2.3.2" % "test",
       "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
)

