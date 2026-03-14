
val scala213Version = "2.13.18"
val scala3Version = "3.3.7"
val scalafixVersion = "0.14.6" // Should be in sync with the version in plugins.sbt too
val crossBuildVersions = Seq(scala213Version, scala3Version)

inThisBuild(
  Seq(
    tlBaseVersion := "0.1",
    organization := "com.github.jatcwang",
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("https://github.com/jatcwang/doobie-package-rename-scalafix")),
    developers := List(
      Developer(
        "jatcwang",
        "Jacob Wang",
        "jatcwang@gmail.com",
        url("https://github.com/jatcwang")
      )
    ),
    
    crossScalaVersions := crossBuildVersions,
    scalaVersion := scala213Version,
    
    githubWorkflowJavaVersions := Seq(JavaSpec.temurin("11"))
  ),

)

lazy val root = tlCrossRootProject
  .aggregate(rules, doobieold, doobienew, input, output, tests)
  .settings(
    commonSettings,
    name := "doobie-package-rename-scalafix"
  )

lazy val rules = project
  .in(file("rules"))
  .settings(
    commonSettings,
    name := "doobie-package-rename-scalafix",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % scalafixVersion
  )

// stub projects for package _root_.doobie
lazy val doobieold = project
  .in(file("doobieold"))
  .enablePlugins(NoPublishPlugin)
  .settings(
    commonSettings,
    crossScalaVersions := crossBuildVersions,
  )

// stub projects for package _root_.org.typelevel.doobie
lazy val doobienew = project
  .in(file("doobienew"))
  .enablePlugins(NoPublishPlugin)
  .settings(
    commonSettings,
    crossScalaVersions := crossBuildVersions,
  )


lazy val input = project
  .in(file("input"))
  .enablePlugins(NoPublishPlugin)
  .dependsOn(doobieold)
  .settings(
    commonSettings,
    semanticdbSettings,
  )
  .settings(
    crossScalaVersions := crossBuildVersions,
  )

lazy val output = project
  .in(file("output"))
  .enablePlugins(NoPublishPlugin)
  .dependsOn(doobienew)
  .settings(
    commonSettings,
    semanticdbSettings,
  )
  .settings(
    crossScalaVersions := crossBuildVersions,
  )

lazy val tests = project
  .in(file("tests"))
  .enablePlugins(NoPublishPlugin, ScalafixTestkitPlugin)
  .dependsOn(rules)
  .settings(
    commonSettings,
    semanticdbSettings,
    publish / skip := true,
    crossScalaVersions := crossBuildVersions,
  )
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
  )

val commonSettings =
  Seq(
    scalaVersion := scala213Version,
  )

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
