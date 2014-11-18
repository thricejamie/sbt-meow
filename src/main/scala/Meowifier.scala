package sbtmeow

import dispatch._
import Defaults._

import javax.imageio._

import java.awt.{Color, RenderingHints}
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream

object Meowifier {
  val asciiChars = List('$','@','B','%','8','&','W','M','#','*','o','a','h','k','b','d','p','q','w','m',
  	'Z','O','0','Q','L','C','J','U','Y','X','z','c','v','u','n','x','r','j','f','t','/','\\','|','(',')',
  	'1','{','}','[',']','?','-','_','+','~','<','>','i','!','l','I',';',':',',','"','^','`',''','.',' ')

  val colors =  List(
  	("00;30", (0, 0, 0)),
  	("01;30", (85, 85, 85)),
  	("00;37", (170, 170, 170)),
  	("01;37", (255, 255, 255)),
  	("00;34", (0, 0, 170)),
  	("01;34", (85, 85, 255)),
  	("00;32", (0, 170, 0)),
  	("01;32", (85, 255, 85)),
  	("00;36", (0, 170, 170)),
  	("01;36", (85, 255, 255)),
  	("00;31", (170, 0, 0)),
  	("01;31", (255, 85, 85)),
  	("00;35", (170, 0, 170)),
  	("01;35", (255, 85, 255)),
  	("00;33", (170, 85, 0)),
  	("01;33", (255, 255, 85))
	)

	def meow = {
		val image = getRandomCatImage
		val scaled = scaledImage(image)
		var textified = textify(scaled)

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
		val darkness = List(color.getRed, color.getGreen, color.getBlue).reduceLeft(Math.max)
		val char = darkness match {
			case 0 => asciiChars.last
			case d => {
				val index = ((asciiChars.length * (d.toDouble / 255)) - (0.5)).toInt
				asciiChars(index)
			}
		}
		s"\033[${colorCode}m$char\033[00m"
	}

	def colorCodeForColor(color: Color): String = {
		val (r, g, b) = (color.getRed, color.getGreen, color.getBlue)
		val withDistances = colors.map(c => (c._1, Math.abs(c._2._1 - r) + Math.abs(c._2._2 - g) + Math.abs(c._2._3 - b)))
		withDistances.sortBy(_._2).head._1
	}
}