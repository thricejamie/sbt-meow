import bintray.Keys._

sbtPlugin := true

name := "sbt-meow"

organization := "com.37pieces"

version := "0.2"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "org.slf4j" % "slf4j-nop" % "1.7.7"
)

publishMavenStyle := false

bintrayPublishSettings

repository in bintray := "sbt-plugins"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayOrganization in bintray := None
