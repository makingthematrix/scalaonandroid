
lazy val `Lazy-Lists` = (project in file("."))
  .settings(
    scalaSource in Compile := baseDirectory.value / "src",
    scalaSource in Test := baseDirectory.value / "test",
    libraryDependencies ++= Seq(
      "org.scala-exercises"        %% "exercise-compiler"         % "0.6.7",
      "org.scala-exercises"        %% "definitions"               % "0.6.7",
      "com.chuusai"                %% "shapeless"                 % "2.3.7",
      "org.scalatest"              %% "scalatest"                 % "3.2.9",
      "org.scalacheck"             %% "scalacheck"                % "1.15.4",
      "org.scalatestplus"          %% "scalacheck-1-14"           % "3.2.2.0",
      "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.5"
    ))