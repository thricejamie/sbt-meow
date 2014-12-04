package sbtmeow

import sbt._
import Keys._

import java.lang.System.{currentTimeMillis => now}

object SbtMeowPlugin extends AutoPlugin {
	override def trigger = allRequirements
  override lazy val projectSettings = Seq(commands += meowCommand)

  lazy val meowCommand = Command.command("meow")(doMeow)

  def doMeow(state: State): State = {
  	state.log.info("Fetching random cat image...")

		val start = now
  	Meowifier.meow
  	state.log.debug("Image fetch took " + (now - start) + "ms")

  	state.log.info("meow.")
  	state
  }
}