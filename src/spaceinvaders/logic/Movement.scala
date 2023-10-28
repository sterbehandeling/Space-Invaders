package spaceinvaders.logic

sealed abstract class Movement {

}

case class MoveLeft() extends Movement {

}

case class MoveRight() extends Movement {

}

case class None() extends Movement {

}
