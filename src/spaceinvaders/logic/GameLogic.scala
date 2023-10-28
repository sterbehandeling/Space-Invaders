package spaceinvaders.logic

import engine.graphics.{Point, Rectangle}
import spaceinvaders.logic.GameLogic._

import scala.collection.mutable
import scala.util.Random

/** To implement Snake, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``snake`` package.
 */
class GameLogic(width : Int, height : Int, startTime : Int) {

  private var player = new Player(Rectangle(Point(width / 2 - 25, 675), 50, 25))
  private var direction : Movement = None()
  private var random = new Random()

  private var enemies : mutable.Seq[mutable.Seq[Alien]] = initAliens()
  private var enemiesLeftSide : Int = 1280 / 2 - 462
  private var lastSampleTimeMvmt : Int = startTime
  private var lastSampleTimeSpawn : Int = startTime
  private var firstFrame : Boolean = true

  private var bullets : mutable.Seq[Bullet] = mutable.Seq[Bullet]()
  private var lastSampleTimeBullet : Int = startTime

  private var score : Float = 0
  private var multiplier : Float = 1
  private var gameover : Boolean = false

  def step(time : Int): Unit = {
    direction match {
      case MoveLeft() if inBoundsCheck(player.moveLeft) => player = player.moveLeft
      case MoveRight() if inBoundsCheck(player.moveRight) => player = player.moveRight
      case _ => {}
    }
    if (firstFrame) {
      initTime(time)
    }

    var newEnemies = mutable.Seq[mutable.Seq[Alien]]()
    if (time - lastSampleTimeMvmt > 3000) {
      newEnemies = alienBehavior(MoveLeft())
      enemiesLeftSide -= 1
    }
    else {
      newEnemies = alienBehavior(MoveRight())
      enemiesLeftSide += 1
    }

    var newBullets = bulletBehavior()

    if (time > lastSampleTimeSpawn + 10000 / multiplier) {
      newEnemies = spawnAlienRow(newEnemies)
      lastSampleTimeSpawn = time
      multiplier = multiplier + 0.2.toFloat
    }

    enemies = newEnemies
    bullets = newBullets

    gameover = checkGameover()

    if (time > lastSampleTimeMvmt + 6000) lastSampleTimeMvmt = time

  }

  private def spawnAlienRow(enemies : mutable.Seq[mutable.Seq[Alien]]) : mutable.Seq[mutable.Seq[Alien]] = {
    val toAdd = initAlienRow(enemiesLeftSide, 50)
    var newEnemies = mutable.Seq[mutable.Seq[Alien]]()
    enemies.foreach({y =>
      var newRow = mutable.Seq[Alien]()
      y.foreach({x =>
        newRow = newRow :+ x.moveDown
      })
      newEnemies = newEnemies :+ newRow
    })
    newEnemies = toAdd +: newEnemies
    newEnemies
  }
  private def checkGameover() : Boolean = {
    if (enemies.flatten.forall(e => e.hitBox.bottom < 675) && bullets.forall({b =>
      if (!b.byPlayer) !collisionsCheck(b.hitBox, player.getHitBox)
      else true
    })) false
    else true
  }

  private def alienBehavior(d : Movement) : mutable.Seq[mutable.Seq[Alien]] = {
    var newEnemies = mutable.Seq[mutable.Seq[Alien]]()
    enemies.foreach({y =>
      var newRow = mutable.Seq[Alien]()
      y.foreach({e =>
        val toAdd = d match {
          case MoveLeft() =>  e.moveLeft
          case MoveRight() => e.moveRight
        }
        if (bullets.forall({b =>
          if (b.byPlayer) !collisionsCheck(b.hitBox, e.hitBox)
          else true
        })) newRow = newRow :+ toAdd
        else score += (100 * multiplier)
      })
      newEnemies = newEnemies :+ newRow
    })

    newEnemies.flatten.foreach({a =>
      if (random.nextInt(10000) > 9993) {
        enemyFireBullet(a.hitBox.centerDown)
      }
    })

    newEnemies
  }
  private def bulletBehavior() : mutable.Seq[Bullet] = {
    var newBullets = mutable.Seq[Bullet]()
    bullets.foreach({b =>
      val toAdd = b.move()
      if (enemies.flatten.forall({a =>
        if (b.byPlayer) !collisionsCheck(a.hitBox, b.hitBox)
        else true
      })) newBullets = newBullets :+ toAdd
    })
    newBullets
  }

  private def collisionsCheck(a : Rectangle, b : Rectangle) : Boolean = {
    if (a.left < b.right && a.right > b.left && a.top < b.bottom && a.bottom > b.top) true
    else false
  }
  private def inBoundsCheck(newPlayer: Player): Boolean = {
    if (newPlayer.getHitBox.left > 10 && newPlayer.getHitBox.right < 1270) true
    else false
  }

  private def enemyFireBullet(p : Point) : Unit = {
    bullets = bullets :+ Bullet(Rectangle(p, 5, 20), byPlayer = false)
  }

  def fireBullet(time : Int) : Unit = {
    if (time > lastSampleTimeBullet + 250) {
      bullets = bullets :+ Bullet(Rectangle(Point(player.getHitBox.centerX - 2, player.getHitBox.top - 20), 5, 20), byPlayer = true)
      lastSampleTimeBullet = time
    }
  }

  def changeDir(d : Movement): Unit = direction = d

  def getPlayerRec : Rectangle = player.getHitBox
  def getAliens : Seq[Rectangle] = {
    var aliens = mutable.Seq[Rectangle]()
    enemies.clone().flatten.foreach({x =>
      aliens = aliens :+ x.hitBox
    })
    aliens.toSeq
  }
  def getBullets : Seq[Rectangle] = {
    var bulletRecs = Seq[Rectangle]()
    bullets.foreach({x =>
      bulletRecs = bulletRecs :+ x.hitBox
    })
    bulletRecs
  }
  def getScore : Float = score
  def getGameover : Boolean = gameover

  def initTime(time : Int) : Unit = {
    lastSampleTimeSpawn = time
    lastSampleTimeBullet = time
    lastSampleTimeMvmt = time - 1500
    firstFrame = false
  }
}

/** GameLogic companion object */
object GameLogic {

  val FramesPerSecond: Int = 60 // change this to increase/decrease speed of game

  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller

  // These are the dimensions used when playing the game.
  // When testing the game, other dimensions are passed to
  // the constructor of GameLogic.
  //
  // DO NOT USE the variable DefaultGridDims in your code!
  //
  // Doing so will cause tests which have different dimensions to FAIL!
  //
  // In your code only use gridDims.width and gridDims.height
  // do NOT use DefaultGridDims.width and DefaultGridDims.height

  def initAliens() : mutable.Seq[mutable.Seq[Alien]] = {
    var y = 50
    var aliens = mutable.Seq[mutable.Seq[Alien]]()
    for (i <- 0 until 4) {
      aliens = aliens :+ initAlienRow(1280 / 2 - 462, y)
      y += 50
    }
    aliens
  }

  def initAlienRow(x : Int, y : Int) : mutable.Seq[Alien] = {
    var nextX = x
    var row = mutable.Seq[Alien]()
    for (i <- 0 until 10) {
      row = row :+ Alien(Rectangle(Point(nextX, y), 32, 25))
      nextX += 100
    }
    row
  }

}

