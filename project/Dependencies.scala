import sbt._
object Dependencies {

  object Version{
    val zio = "2.0.7"
    val `zio-logging` = "2.1.8"
    val `zio-http` = "0.0.5"
    val tapir = "1.2.8"
    val `akka-http` = "10.2.10"
    val akka = "2.6.20"
    val circe = "0.14.3"
    val logback = "1.3.5"
    val config = "1.4.2"
    val `zio-interop-reactiveStreams` = "2.0.0"
    val scalaTest = "3.2.14"
    val `zio-actors` = "0.1.0"
    val refined = "0.10.1"
    val `zio-schema` = "0.3.1"
    val `sttp-apispec` = "0.3.1"
    val `slf4j-simple` = "2.0.5"
    val `os-lib` = "0.9.0"
    val http4sBlazeVersion  = "0.23.13"
    val `scala-csv` = "1.3.10"
    val purecsv = "1.3.10"
    val `zio-config` = "3.0.7"
    val `zio-json`  = "0.4.2"
    val `zio-interop-cats` = "23.0.0.1"
  }

  lazy val zioDeps: Seq[ModuleID] = Seq(
    "dev.zio" %% "zio"                         % Version.zio,
    "dev.zio" %% "zio-logging"                 % Version.`zio-logging`,
    "dev.zio" %% "zio-logging-slf4j"           % Version.`zio-logging`,
    "dev.zio" %% "zio-test"                    % Version.zio % Test,
    "dev.zio" %% "zio-http"                    % Version.`zio-http`,
    "dev.zio" %% "zio-streams"                 % Version.zio,
    "dev.zio" %% "zio-macros"                  % Version.zio,
    "dev.zio" %% "zio-config"                  % Version.`zio-config`,
    "dev.zio" %% "zio-config-magnolia"         % Version.`zio-config`,
    "dev.zio" %% "zio-config-typesafe"         % Version.`zio-config`,
//    "dev.zio" %% "zio-json"                    % Version.`zio-json`,
    "dev.zio" %% "zio-interop-cats"            % Version.`zio-interop-cats`
  )
  lazy val tapirDeps: Seq[ModuleID] = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core"              % Version.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"      % Version.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server"  % Version.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe"        % Version.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % Version.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-asyncapi-docs"     % Version.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-zio"               % Version.tapir,
//    "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server"   % Version.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server-zio" % Version.tapir
  )

  lazy val akkaDeps: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor-typed"    % Version.akka,
    "com.typesafe.akka" %% "akka-http"           % Version.`akka-http`,
    "com.typesafe.akka" %% "akka-stream"         % Version.akka,
    "com.typesafe.akka" %% "akka-slf4j"          % Version.akka,
    "com.typesafe.akka" %% "akka-stream-testkit" % Version.akka % Test,
    "com.typesafe.akka" %% "akka-http-testkit"   % Version.`akka-http` % Test
  )

  lazy val circeDeps: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-generic"        % Version.circe,
    "io.circe" %% "circe-generic-extras" % Version.circe,
    "io.circe" %% "circe-parser"         % Version.circe
  )

  lazy val otherDeps: Seq[ModuleID] = Seq(
    "ch.qos.logback"              % "logback-classic"      % Version.logback,
//    "com.typesafe"                % "config"               % Version.config,
    "org.slf4j"                   % "slf4j-simple"         % Version.`slf4j-simple`,
    "org.scalatest"               %% "scalatest"           % Version.scalaTest % Test,
    "com.lihaoyi"                 %% "os-lib"              % Version.`os-lib`,
    "org.http4s"                  %% "http4s-blaze-server" % Version.http4sBlazeVersion,
    "org.http4s"                  %% "http4s-blaze-client" % Version.http4sBlazeVersion,
    "org.http4s"                  %% "http4s-circe"        % Version.http4sBlazeVersion,
    "com.github.tototoshi"        %% "scala-csv"           % Version.`scala-csv`,
    "io.kontainers"               %% "purecsv"             % Version.purecsv
  )

  lazy val scalaJS : Seq[ModuleID] = Seq(

  )
}
