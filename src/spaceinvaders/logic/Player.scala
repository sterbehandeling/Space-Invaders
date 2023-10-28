package spaceinvaders.logic
import engine.graphics.{Point, Rectangle}

class Player(hitBox : Rectangle) {
  def getHitBox : Rectangle = hitBox
  def moveLeft : Player =
    new Player(Rectangle(Point((this.hitBox.leftUp.x - 5), this.hitBox.leftUp.y), this.hitBox.width, this.hitBox.height))
  def moveRight : Player =
    new Player(Rectangle(Point((this.hitBox.leftUp.x + 5), this.hitBox.leftUp.y), this.hitBox.width, this.hitBox.height))

}
