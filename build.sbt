val scala2Version = "2.13.3"

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.9" % Test
val cats      = "org.typelevel" %% "cats-effect" % "3.1.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "shopping-basket",
    version := "0.1.0",

    libraryDependencies ++= Seq(scalaTest, cats),

    scalaVersion := scala2Version
  )