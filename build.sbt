val helidonVersion = "4.1.6"
val scalafxVersion = "23.0.1-R34"
val logbackVersion = "1.5.16"
val scalatestVersion = "3.2.19"
val oxVersion = "0.5.10"

lazy val common = Defaults.coreDefaultSettings ++ Seq(
  organization := "objektwerks",
  version := "0.32-SNAPSHOT",
  scalaVersion := "3.6.4-RC1",
  scalacOptions ++= Seq(
    "-Wall"
  )
)

lazy val swimmer = (project in file("."))
  .aggregate(client, shared, server)
  .settings(common)
  .settings(
    publish := {},
    publishLocal := {},
  )

// Begin: Assembly Tasks
  lazy val createAssemblyDir = taskKey[File]("Create assembly dir.")
  createAssemblyDir := {
    import java.nio.file._

    val assemblyDir: File = baseDirectory.value / ".assembly"
    val assemblyPath: Path = assemblyDir.toPath()

    if (!Files.exists(assemblyPath)) Files.createDirectory(assemblyPath)

    println(s"[createAssemblyDir] assembly dir: ${assemblyPath} is valid: ${Files.isDirectory(assemblyPath)}")

    assemblyDir
  }

  lazy val copyAssemblyJar = taskKey[Unit]("Copy assembly jar to assembly dir.")
  copyAssemblyJar := {
    import java.nio.file._

    val assemblyDir: File = createAssemblyDir.value
    val assemblyPath: String = s"${assemblyDir.toString}/${assemblyJarName.value}"

    val source: Path = (assembly / assemblyOutputPath).value.toPath
    val target: Path = Paths.get(assemblyPath)

    println(s"[copyAssemblyJar] source: ${source}")
    println(s"[copyAssemblyJar] target: ${target}")

    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING)
  }
// End: Assembly Tasks

// Begin: Assembly
assemblyJarName := s"brewmeister-${version.value}.jar"
assembly / assemblyMergeStrategy := {
  case PathList("META-INF",  xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
// End: Assembly

lazy val client = project
  .dependsOn(shared)
  .settings(common)
  .settings(
    libraryDependencies ++= {
      Seq(
        "org.scalafx" %% "scalafx" % scalafxVersion,
        "io.helidon.webclient" % "helidon-webclient" % helidonVersion,
        "com.typesafe" % "config" % "1.4.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
        "ch.qos.logback" % "logback-classic" % logbackVersion
      )
    }
  )
  .settings(
    libraryDependencies ++= Seq("base", "controls", "web").map( jfxModule =>
      "org.openjfx" % s"javafx-$jfxModule" % "23" classifier OS
    )
  )

lazy val shared = project
  .settings(common)
  .settings(
    libraryDependencies ++= {
      val jsoniterVersion = "2.33.1"
      Seq(
        "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % jsoniterVersion,
        "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % jsoniterVersion % Provided,
        "org.scalafx" %% "scalafx" % scalafxVersion
         exclude("org.openjfx", "javafx-controls")
         exclude("org.openjfx", "javafx-fxml")
         exclude("org.openjfx", "javafx-graphics")
         exclude("org.openjfx", "javafx-media")
         exclude("org.openjfx", "javafx-swing")
         exclude("org.openjfx", "javafx-web"),
        "org.scalatest" %% "scalatest" % scalatestVersion % Test
      )
    }
  )

lazy val server = project
  .enablePlugins(JavaServerAppPackaging)
  .dependsOn(shared)
  .settings(common)
  .settings(
    libraryDependencies ++= {
      Seq(
        "io.helidon.webserver" % "helidon-webserver" % helidonVersion,
        "com.softwaremill.ox" %% "core" % oxVersion,
        "org.scalikejdbc" %% "scalikejdbc" % "4.3.2",
        "com.zaxxer" % "HikariCP" % "6.2.1" exclude("org.slf4j", "slf4j-api"),
        "org.postgresql" % "postgresql" % "42.7.5",
        "com.github.blemale" %% "scaffeine" % "5.2.1",
        "org.jodd" % "jodd-mail" % "7.1.0",
        "com.typesafe" % "config" % "1.4.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
        "ch.qos.logback" % "logback-classic" % logbackVersion,
        "org.scalatest" %% "scalatest" % scalatestVersion % Test
      )
    }
  )
