val helidonVersion = "4.0.11"
val scalafxVersion = "22.0.0-R33"
val logbackVersion = "1.5.6"
val scalatestVersion = "3.2.19"

lazy val common = Defaults.coreDefaultSettings ++ Seq(
  organization := "objektwerks",
  version := "0.28-SNAPSHOT",
  scalaVersion := "3.5.0-RC6",
  scalacOptions ++= Seq(
    "-Wunused:all"
  )
)

lazy val swimmer = (project in file("."))
  .aggregate(client, shared, server)
  .settings(common)
  .settings(
    publish := {},
    publishLocal := {},
  )

// Begin: Client Assembly Tasks

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

// End: Client Assembly Tasks

// Begin: Client Assembly

  /*
  See assembly section in readme.
  1. sbt -Dtarget="mac" clean test assembly copyAssemblyJar
  2. sbt -Dtarget="m1" clean test assembly copyAssemblyJar
  3. sbt -Dtarget="win" clean test assembly copyAssemblyJar
  4. sbt -Dtarget="linux" clean test assembly copyAssemblyJar
  */
  lazy val OS: String = sys.props.getOrElse("target", "") match {
    case name if name.startsWith("mac")   => "mac"
    case name if name.startsWith("m1")    => "mac-aarch64"
    case name if name.startsWith("win")   => "win"
    case name if name.startsWith("linux") => "linux"
    case _ => ""
  }

  if (OS == "mac") assemblyJarName := "swimmer-mac-0.21.jar"
  else if (OS == "mac-aarch64") assemblyJarName := "swimmer-m1-0.21.jar"
  else if (OS == "win") assemblyJarName := "swimmer-win-0.21.jar"
  else if (OS == "linux") assemblyJarName := "swimmer-linux-0.21.jar"
  else assemblyJarName := "swimmer-no-valid-target-specified-0.21.jar"

  client / assembly / assemblyMergeStrategy := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  }

// End: Client Assembly

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
      "org.openjfx" % s"javafx-$jfxModule" % "21" classifier OS
    )
  )

lazy val shared = project
  .settings(common)
  .settings(
    libraryDependencies ++= {
      val jsoniterVersion = "2.30.7"
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
        "org.scalikejdbc" %% "scalikejdbc" % "4.3.0",
        "com.zaxxer" % "HikariCP" % "5.1.0" exclude("org.slf4j", "slf4j-api"),
        "org.postgresql" % "postgresql" % "42.7.3",
        "com.github.blemale" %% "scaffeine" % "5.2.1",
        "org.jodd" % "jodd-mail" % "7.0.1",
        "com.typesafe" % "config" % "1.4.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
        "ch.qos.logback" % "logback-classic" % logbackVersion,
        "org.scalatest" %% "scalatest" % scalatestVersion % Test
      )
    }
  )
