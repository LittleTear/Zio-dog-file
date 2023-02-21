ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"


// sbt-assembly, not support build docker image by sbt task, so we use sbt-native-packager
lazy val assemblySettings = Seq(
  ThisBuild / assemblyMergeStrategy := {
    case PathList("META-INF", _*)                          => MergeStrategy.discard
    case "deriving.conf"                                   => MergeStrategy.first
    case "reference.conf"                                  => MergeStrategy.first
    case x if x.endsWith("module-info.class")            => MergeStrategy.first
    case x if x.endsWith(".properties")                  => MergeStrategy.deduplicate
    case x if x.endsWith(".conf")                        => MergeStrategy.deduplicate
    case x =>
      val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
      oldStrategy(x)
  }
)

lazy val root = (project in file("."))
  .settings(name := "zio-dog-file")
  .aggregate(`file-http`)
  .settings(assemblySettings)

 lazy val `file-http` = (project in file("modules/file-http"))
   .settings(
     libraryDependencies ++=
       (Dependencies.zioDeps
         ++ Dependencies.tapirDeps
         ++ Dependencies.circeDeps
         ++ Dependencies.otherDeps),
         Compile / scalacOptions ++= List("-Ymacro-annotations"),
         Compile / mainClass := Some("org.littletear.dogfile.ServerMain"),
         assembly / mainClass := Some("ServerMain"),
   )
   .settings(assemblySettings)


