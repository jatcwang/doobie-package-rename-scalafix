
name := "doobie-scalafix"

ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.18"

val scalafixVersion = "0.14.6" // Should be in sync with the version in plugins.sbt too

lazy val rules = project
  .in(file("rules"))
  .settings(
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % scalafixVersion
  )

lazy val input = project
  .in(file("input"))
  .settings(
    scalacOptions += "-Yrangepos",
    //addCompilerPlugin("org.scalameta" % "semanticdb-scalac" % "4.8.11" cross CrossVersion.full),
  )

lazy val output = project
  .in(file("output"))
  .settings(
    scalacOptions += "-Yrangepos"
  )

lazy val tests = project
  .in(file("tests"))
  .settings(
    libraryDependencies ++= Seq(
      "ch.epfl.scala" %% "scalafix-testkit" % scalafixVersion % Test cross CrossVersion.full,
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    ),
    scalafixTestkitOutputSourceDirectories :=
      (output / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (input / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (input / Compile / fullClasspath).value
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
