# Doobie package rename scalafix rule

Doobie has moved under the Typelevel organisation and as part of that
doobie will be published under `org.typelevel` instead of `org.tpolecat`.

To avoid breakages, it is necessary for us to rename any references to
`_root_.doobie` to `org.typelevel.doobie`.

This Scalafix rule will perform that rename for you across your project.

## Running this migration scalafix rule 

To run the rule against another sbt project:

1. Add `sbt-scalafix` to `project/plugins.sbt`:

   ```scala
   addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.14.6")
   ```

2. Add this rule as a Scalafix dependency in `build.sbt`:

   ```scala
   scalafixDependencies +=
     "com.github.jatcwang" %% "doobie-package-rename-scalafix" % "0.1.1"
   ```

3. Enable SemanticDB for the project you want to rewrite.

   For Scala 2:

   ```scala
   semanticdbEnabled := true
   semanticdbVersion := scalafixSemanticdb.revision
   scalacOptions += "-Yrangepos"
   ```

   For Scala 3:

   ```scala
   scalacOptions += "-Xsemanticdb"
   ```

4. Compile the project, then run the rule:

   ```sh
   sbt --client "compile ; scalafix DoobiePackageRename"
   ```
