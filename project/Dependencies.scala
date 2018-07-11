import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % Test
  lazy val scalaMock = "org.scalamock" %% "scalamock" % "4.1.0" % Test

  lazy val cats = "org.typelevel" %% "cats-core" % "1.0.1"
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "1.0.0-RC2"
  lazy val monix = "io.monix" %% "monix" % "3.0.0-RC1"
  lazy val doobie = "org.tpolecat" %% "doobie-core" % "0.5.3"
}
