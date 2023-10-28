package spaceinvaders.logic

import engine.graphics.{Point, Rectangle}

case class Bullet(hitBox : Rectangle, byPlayer : Boolean) {
  def move() : Bullet = {
    if (this.byPlayer) Bullet(Rectangle(Point(this.hitBox.leftUp.x, this.hitBox.leftUp.y - 5), this.hitBox.width, this.hitBox.height ), this.byPlayer)
    else Bullet(Rectangle(Point(this.hitBox.leftUp.x, this.hitBox.leftUp.y + 5), this.hitBox.width, this.hitBox.height ), this.byPlayer)
  }

}
