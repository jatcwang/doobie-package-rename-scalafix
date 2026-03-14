
name := "doobie-scalafix-rename-package"

ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.18"

val scalafixVersion = "0.14.6" // Should be in sync with the version in plugins.sbt too

lazy val rules = project
  .in(file("rules"))
  .settings(
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % scalafixVersion
  )

// stub projects for package _root_.doobie
lazy val doobieold = project
  .in(file("doobieold"))
  .settings(publish / skip := true)

// stub projects for package _root_.org.typelevel.doobie
lazy val doobienew = project
  .in(file("doobienew"))
  .settings(publish / skip := true)


lazy val input = project
  .in(file("input"))
  .dependsOn(doobieold)
  .settings(
    semanticdbSettings,
    publish / skip := true
  )

lazy val output = project
  .in(file("output"))
  .dependsOn(doobienew)
  .settings(
    scalacOptions += "-Yrangepos",
    semanticdbSettings,
    publish / skip := true
  )

lazy val tests = project
  .in(file("tests"))
  .settings(
    libraryDependencies ++= Seq(
      "ch.epfl.scala" %% "scalafix-testkit" % scalafixVersion % Test cross CrossVersion.full,
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    ),
    scalafixTestkitInputScalacOptions :=
      (input / Compile / scalacOptions).value,
    scalafixTestkitInputScalaVersion :=
      (input / Compile / scalaVersion).value,
    scalafixTestkitOutputSourceDirectories :=
      (output / Compile / sourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (input / Compile / sourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (input / Compile / fullClasspath).value,
    semanticdbSettings,
    publish / skip := true
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)

lazy val semanticdbSettings = Def.settings(
  semanticdbVersion := scalafixSemanticdb.revision,
  libraryDependencies ++= {
    scalaBinaryVersion.value match {
      case "3" =>
        Nil
      case _ =>
        Seq(compilerPlugin(scalafixSemanticdb))
    }
  },
  scalacOptions ++= {
    scalaBinaryVersion.value match {
      case "3" =>
        List(
          "-Xsemanticdb"
        )
      case _ =>
        List(
          "-Yrangepos",
          "-P:semanticdb:synthetics:on"
        )
    }
  },
)
