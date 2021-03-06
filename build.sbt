import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val CirceVersion = "0.13.0"
val ScalatestVersion = "3.2.3"

ThisBuild / githubProject := "circe-validation"

noPublishSettings

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "io.circe" %%% "circe-core" % CirceVersion ::
        "io.circe" %%% "circe-generic" % CirceVersion % "test" ::
        "org.scalatest" %%% "scalatest-flatspec" % ScalatestVersion % "test" ::
        "org.scalatest" %%% "scalatest-shouldmatchers" % ScalatestVersion % "test" ::
        Nil,
    name := "circe-validation",
    normalizedName := name.value
  )

lazy val website = project
  .enablePlugins(MicrositesPlugin)
  .settings(micrositeSettings)
  .settings(
    libraryDependencies ++=
      "io.circe" %% "circe-generic" % CirceVersion % "compile" ::
        "io.circe" %% "circe-parser" % CirceVersion % "compile" ::
        Nil,
    mdocVariables ++= {
      val format: String => String =
        version => s"`${version.replaceAll("\\.\\d+$", "")}`"

      Map(
        "NAME" -> micrositeName.value,
        "MODULE" -> (core.jvm / normalizedName).value,
        "SCALA_VERSIONS" -> crossScalaVersions.value.map(format).mkString(", "),
        "SCALAJS_VERSION" -> format(scalaJSVersion)
      )
    },
    micrositeDescription := "Use cats Validated to create (Accumulating) circe Decoders",
    micrositeName := "circe Validation"
  )
  .dependsOn(core.jvm)
