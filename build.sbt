name := "Logger"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("logger")

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"
libraryDependencies += "org.scalamock" %% "scalamock" % "5.1.0"  % Test


lazy val coverageSettings = Seq(
  coverageEnabled := true,
  coverageFailOnMinimum := true,
  coverageMinimumStmtTotal := 100
)

lazy val root = (project in file("."))
  .settings(coverageSettings)
  .enablePlugins(JavaAppPackaging)