
lazy val `Recursive-Function-Application` = (project in file("."))
  .settings(
    scalaSource in Compile := baseDirectory.value / "src",
    scalaSource in Test := baseDirectory.value / "test",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5"
  )
