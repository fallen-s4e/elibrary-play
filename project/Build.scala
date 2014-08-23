import play.Project._
import sbt._

object ApplicationBuild extends Build {

  val appName         = "first-web-scala-project"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "com.typesafe.slick" %% "slick" % "2.1.0",
    "com.h2database" % "h2" % "1.3.175"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
