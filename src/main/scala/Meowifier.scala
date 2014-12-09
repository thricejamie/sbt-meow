package sbtmeow

import dispatch._
import Defaults._

import javax.imageio._

import java.awt.{Color, RenderingHints}
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream

object Meowifier {

  def meow = {
    val image = getRandomCatImage
    val scaled = scaledImage(image)
    val textified = textify(scaled)

    println(textified)
  }

  def getRandomCatImage: BufferedImage = {
    val requestUrl = url("http://thecatapi.com/api/images/get?type=jpg")
    val request = Http.configure(_ setFollowRedirects true)(requestUrl OK as.Bytes)
    val bytes = request()
    ImageIO.read(new ByteArrayInputStream(bytes))
  }

  def scaledImage(image: BufferedImage, maxwidth: Int = 120): BufferedImage = {
    val scaledWidth = Math.min(image.getWidth, maxwidth)
    val scaledHeight = ((maxwidth.toDouble / image.getWidth) * image.getHeight / 2).toInt
    val scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB)
    val gfx = scaledImage.createGraphics()
    gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    gfx.drawImage(image, 0, 0, scaledWidth, scaledHeight, null)
    gfx.dispose
    scaledImage
  }

  def textify(image: BufferedImage): String = {
    val lines = makeLines(image)
    lines.map(_.mkString).mkString("\n")
  }

  def makeLines(image: BufferedImage): Seq[Seq[String]] = {
    (0 until image.getHeight) map { y =>
      (0 until image.getWidth) map { x =>
        val pixelColor = new Color(image.getRGB(x, y))
        charForColor(pixelColor)
      }
    }
  }

  def charForColor(color: Color): String = {
    val colorCode = colorCodeForColor(color)
    s"\033[48;5;${colorCode}m \033[00m"
  }

  def colorCodeForColor(color: Color): String = {
    val (r, g, b) = (color.getRed, color.getGreen, color.getBlue)
    val withDistances = Colors.lookup.map(c => (c._1, Math.abs(c._2._1 - r) + Math.abs(c._2._2 - g) + Math.abs(c._2._3 - b)))
    withDistances.sortBy(_._2).head._1
  }
}