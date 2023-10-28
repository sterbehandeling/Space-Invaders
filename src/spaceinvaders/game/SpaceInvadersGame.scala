// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package spaceinvaders.game

import apple.laf.JRSUIConstants.Direction

import java.awt.event
import processing.core.{PApplet, PConstants, PImage}
import processing.event.KeyEvent

import java.awt.event.KeyEvent._
import engine.GameBase
import engine.graphics.{Color, Point, Rectangle}
import spaceinvaders.logic._
import spaceinvaders.game.SpaceInvadersGame._
import engine.graphics.Color._
import sun.security.ec.point.ProjectivePoint.Mutable

import java.io.{File, FileWriter}
import scala.collection.mutable
import scala.io.Source



class SpaceInvadersGame extends GameBase {

  val updateTimer = new UpdateTimer(GameLogic.FramesPerSecond.toFloat)
  val widthInPixels: Int = (GameLogic.DrawSizeFactor * WidthInPixels).ceil.toInt
  val heightInPixels: Int = (GameLogic.DrawSizeFactor *  HeightInPixels).ceil.toInt
  val screenArea: Rectangle = Rectangle(Point(0, 0), widthInPixels.toFloat, heightInPixels.toFloat)
  val centerOfScreen : Point = Point(widthInPixels / 2, heightInPixels / 2)
  val titlePos : Point = Point(widthInPixels / 2, heightInPixels / 5)
  val playPos : Point = Point(widthInPixels / 2, heightInPixels / 2 - 25)
  val exitPos : Point = Point(widthInPixels / 2, heightInPixels / 2 + 12)
  val leaderboardPos : Point = Point(widthInPixels / 2, heightInPixels / 2 + 50)
  var gameLogic = new GameLogic(widthInPixels, heightInPixels, currentTime())
  var titleScreenLogic = new TitleScreenLogic(widthInPixels, heightInPixels)
  var inTitleScreen = true
  var inLeaderboardScreen = false
  var leaderboard = mutable.Seq[Int]()
  var timeSinceGameover = currentTime()

  val sprites : Array[PImage] = new Array[PImage](4)
  val spritePaths : Array[String] = Array(
    "sprites/bg.png",
    "sprites/player.png",
    "sprites/alien.png",
    "sprites/title.png"
  )

  val fw = new FileWriter(new File("leaderboard.txt"), true)
  fw.close()

  val fr = Source.fromFile("leaderboard.txt")
  for (line <- fr.getLines()) {
    leaderboard = leaderboard :+ line.toInt
  }
  leaderboard = leaderboard.sortWith(_ > _)
  fr.close()

  // this function is wrongly named draw by processing (is called on each update next to drawing)
  override def draw(): Unit = {
    updateState()
    drawGrid()
    if (gameLogic.getGameover) {
      if (timeSinceGameover + 6000 < currentTime()) timeSinceGameover = currentTime()
      setFillColor(White)
      drawTextCentered("Gameover!", 50, titlePos)
      if (timeSinceGameover + 5000 < currentTime()) {
        timeSinceGameover = currentTime()
        val score = gameLogic.getScore.toInt
        if (score != 0) writeAndUpdateScore(score)
        gameLogic = new GameLogic(widthInPixels, heightInPixels, currentTime())
        inTitleScreen = true
        inLeaderboardScreen = true
      }
    }
  }

  def drawGrid(): Unit = {
    background(sprites(0))

    if (inLeaderboardScreen) drawLeaderboard()
    else if (inTitleScreen) drawTitleScreen()
    else drawGame()

  }

  def drawLeaderboard() : Unit = {
    setFillColor(White)
    drawText("Press Space to exit", Point(100, 10), 20)
    leaderboard.sorted
    var height = 50

    leaderboard.foreach({x =>
      drawText(x.toString, Point(widthInPixels / 2, height), 20)
      height += 30
    })

  }

  def drawTitleScreen() : Unit = {
    setFillColor(White)
    drawSprite(Rectangle(Point(widthInPixels / 2 - 175, 40), 350, 195), sprites(3))
    drawTextCentered("Play", 20, playPos)
    drawTextCentered("Quit", 20, exitPos)
    drawTextCentered("Leaderboard", 20, leaderboardPos)
    drawSprite(titleScreenLogic.getSelect, sprites(2))
  }

  def drawGame() : Unit = {
    drawSprite(gameLogic.getPlayerRec, sprites(1))

    gameLogic.getAliens.foreach(drawSprite(_, sprites(2)))

    setFillColor(Red)
    gameLogic.getBullets.foreach(drawRectangle)

    setFillColor(White)
    val score = "Score: " + gameLogic.getScore.toInt.toString
    drawText(score, Point(1150, 10), 20)
  }

  def switchTitleScreen() = inTitleScreen = !inTitleScreen
  def switchLeaderboardScreen() = inLeaderboardScreen = !inLeaderboardScreen

  def writeAndUpdateScore(score : Int) : Unit = {
    val fw = new FileWriter(new File("leaderboard.txt"), true)
    fw.write((score.toString + "\n"))
    fw.close()

    val fr = Source.fromFile("leaderboard.txt")
    leaderboard = mutable.Seq[Int]()
    for (line <- fr.getLines()) {
      leaderboard = leaderboard :+ line.toInt
    }
    leaderboard = leaderboard.sortWith(_ > _)
    fr.close()
  }


  /** Method that calls handlers for different key press events.
   * You may add extra functionality for other keys here.
   * See [[event.KeyEvent]] for all defined keycodes.
   *
   * @param event The key press event to handle
   */
  override def keyPressed(event: KeyEvent): Unit = {

    if (inLeaderboardScreen) {
      event.getKeyCode match {
        case VK_SPACE => switchLeaderboardScreen()
        case _ => ()
      }
    }
    else if (inTitleScreen) {
      event.getKeyCode match {
        case VK_UP => titleScreenLogic.changeSelect(MoveLeft())
        case VK_DOWN => titleScreenLogic.changeSelect(MoveRight())
        case VK_SPACE => {
          titleScreenLogic.currSelect match {
            case 0 => switchTitleScreen()
            case 1 => sys.exit()
            case 2 => switchLeaderboardScreen()
            case _ => {}
          }
        }
        case _ => ()
      }
    }
    else {
      event.getKeyCode match {
        case VK_LEFT => gameLogic.changeDir(MoveLeft())
        case VK_RIGHT => gameLogic.changeDir(MoveRight())
        case VK_SPACE => gameLogic.fireBullet(currentTime())
        case _ => ()
      }
    }

  }

  override def keyReleased(event: KeyEvent): Unit = {
    if (!inTitleScreen && !inLeaderboardScreen) {
      event.getKeyCode match {
        case VK_LEFT => gameLogic.changeDir(None())
        case VK_RIGHT => gameLogic.changeDir(None())
        case _ => ()
      }
    }
  }

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    // If line below gives errors try size(widthInPixels, heightInPixels, PConstants.P2D)
    size(widthInPixels, heightInPixels)
  }

  override def setup(): Unit = {
    // Fonts are loaded lazily, so when we call text()
    // for the first time, there is significant lag.
    // This prevents it from happening during gameplay.
    text("", 0, 0)

    for (i <- sprites.indices) {
      sprites(i) = loadImage(spritePaths(i))
    }

    // This should be called last, since the game
    // clock is officially ticking at this point
    updateTimer.init()
  }


  def updateState(): Unit = {
    if (updateTimer.timeForNextFrame() && !inLeaderboardScreen && !inTitleScreen) {
      if (!gameLogic.getGameover) gameLogic.step(currentTime())
      updateTimer.advanceFrame()
    }
  }

}


object SpaceInvadersGame {
  val WidthInPixels: Double = 1280 * GameLogic.DrawSizeFactor
  val HeightInPixels: Double = 720 * GameLogic.DrawSizeFactor

  def main(args: Array[String]): Unit = {
    // This is needed for Processing, using the name
    // of the class in a string is not very beautiful...
    PApplet.main("spaceinvaders.game.SpaceInvadersGame")
  }

}
