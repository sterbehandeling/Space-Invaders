package spaceinvaders.logic

import engine.graphics.{Point, Rectangle}

case class Alien(hitBox : Rectangle) {

  def moveLeft: Alien =
    Alien(Rectangle(Point((this.hitBox.leftUp.x - 1), this.hitBox.leftUp.y), this.hitBox.width, this.hitBox.height))

  def moveRight: Alien =
    Alien(Rectangle(Point((this.hitBox.leftUp.x + 1), this.hitBox.leftUp.y), this.hitBox.width, this.hitBox.height))

  def moveDown: Alien =
    Alien(Rectangle(Point(this.hitBox.leftUp.x, this.hitBox.leftUp.y + 50), this.hitBox.width, this.hitBox.height))

}
