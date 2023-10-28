package spaceinvaders.logic
import engine.graphics.{Point, Rectangle}

class TitleScreenLogic(width : Int, height : Int) {
  var currSelect : Int = 0
  val playRec : Rectangle = Rectangle(Point(width / 2 - 35, height / 2 - 38), 10, 10)
  val quitRec : Rectangle = Rectangle(Point(width / 2 - 35, height / 2), 10, 10)
  val leaderboardRec : Rectangle = Rectangle(Point(width / 2 - 75, height / 2 + 38), 10, 10)


  def changeSelect(d : Movement) : Unit = {
    d match {
      case MoveLeft() if (currSelect != 0) => currSelect -= 1
      case MoveRight() if (currSelect != 2) => currSelect += 1
      case _ => {}
    }
  }

  def getSelect : Rectangle = {
    currSelect match {
      case 0 => playRec
      case 1 => quitRec
      case 2 => leaderboardRec
    }
  }


}
