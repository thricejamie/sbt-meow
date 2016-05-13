package sbtmeow

import sbt._
import Keys._

import java.lang.System.{currentTimeMillis => now}

object SbtMeowPlugin extends AutoPlugin {
  override def trigger = allRequirements
  override lazy val projectSettings = Seq(commands += meowCommand)

  lazy val meowCommand = Command.command("meow")(doMeow)

  def doMeow(state: State): State = {
    state.log.info("(^._.^)ﾉ Fetching random cat image...")

    try {
      val start = now
      Meowifier.meow
      state.log.debug(s"Image fetch took ${(now - start)}ms")
      
      state.log.info("(^._.^)ﾉ meow.")
    } catch {
      case t: Throwable => state.log.warn(s"(^._.^)ﾉ Can not currently meow: ${t.getMessage}")
    }

    state
  }
}