
name := "colossus-helloworld-service"

version := "0.1.0"

scalaVersion := "2.12.4"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")


resolvers in ThisBuild ++= Seq(
  "repo1" at "https://repo1.maven.org/maven2/"
)


val colossusV = "0.11.0"
val akkaV = "2.5.6"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.tumblr" %% "colossus" % colossusV,
)