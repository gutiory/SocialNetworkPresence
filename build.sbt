lazy val root = (project in file("."))
  .settings(
    organization := "com.gutiory",
    name         := "SocialNetworkPresence",
    version      := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.8",
    libraryDependencies ++= Seq(
      "org.http4s"                  %% "http4s-ember-server" % "0.23.10",
      "org.http4s"                  %% "http4s-ember-client" % "0.23.10",
      "org.http4s"                  %% "http4s-circe"        % "0.23.10",
      "org.http4s"                  %% "http4s-dsl"          % "0.23.10",
      "io.circe"                    %% "circe-generic"       % "0.14.1",
      "io.chrisdavenport"           %% "mules"               % "0.5.0",
      "com.softwaremill.sttp.tapir" %% "tapir-core"          % "0.20.1",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"    % "0.20.1",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "0.20.1",
      "com.softwaremill.sttp.tapir" %% "tapir-enumeratum"    % "0.20.1",
      "com.beachape"                %% "enumeratum"          % "1.7.0",
      "com.beachape"                %% "enumeratum-circe"    % "1.7.0",
      "org.scalameta"               %% "munit"               % "0.7.29" % Test,
      "org.typelevel"               %% "munit-cats-effect-3" % "1.0.7"  % Test,
      "ch.qos.logback"               % "logback-classic"     % "1.2.10" % Runtime,
      "org.scalameta"               %% "svm-subs"            % "20.2.0"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )
