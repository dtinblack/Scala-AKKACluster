name := "PiCluster"

version := "1.0"

scalaVersion := "2.10.3"

sbtVersion := "0.13.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
       "com.typesafe.akka" %% "akka-actor"   % "2.2-M3",
       "com.typesafe.akka" %% "akka-remote"  % "2.2-M3",
       "com.typesafe.akka" %% "akka-cluster" % "2.3.2"
)
