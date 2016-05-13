package sbtmeow

import sbt._
import Keys._

import java.lang.System.{currentTimeMillis => now}

object SbtMeowPlugin extends AutoPlugin {
  override def trigger = allRequirements
  override lazy val projectSettings = Seq(commands += meowCommand)

  object autoImport {
    val meowCatApiKey = settingKey[String]("Specifies an optional API key for TheCatApi.com")
  }

  // The internet said to do this ¯\_(ツ)_/¯
  import autoImport._

  lazy val meowCommand = Command.command("meow")(doMeow)

  def doMeow(state: State): State = {
    val extracted = Project.extract(state)
    import extracted._

    state.log.info("(^._.^)ﾉ Fetching random cat image...")

    try {
      var apiKey = (meowCatApiKey in currentRef get structure.data)

      val start = now
      Meowifier.meow(apiKey)
      state.log.debug(s"Image fetch took ${(now - start)}ms")

      state.log.info("(^._.^)ﾉ meow.")
    } catch {
      case t: Throwable => state.log.warn(s"(^._.^)ﾉ Can not currently meow: ${t.getMessage}")
    }

    state
  }
}