ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

val akkaVersion = "10.6.0-M1"
val akkaStreamVersion = "2.9.0-M2"
val logbackVersion = "1.5.9"
val configVersion = "1.4.3"
val scalaTestVersion = "3.2.19"
val sprayVersion = "1.3.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaStreamVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "com.typesafe" % "config" % configVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "io.spray" %% "spray-json" % sprayVersion,
)

lazy val root = (project in file("."))
  .settings(
    name := "llm-restful-microservice"
  )

assembly / assemblyMergeStrategy := {
  case "module-info.class" => MergeStrategy.discard // Discards all occurrences of module-info.class
  case x => (assembly / assemblyMergeStrategy).value(x) // Fallback to default strategy
}