rm -r codegradedist
mkdir -p codegradedist
cd codegradedist
wget https://gitlab.com/vu-oofp/gamebase/-/archive/snake/gamebase-snake.tar.gz
tar -xvf gamebase-snake.tar.gz --strip-components=1
rm gamebase-snake.tar.gz
rm -r lib
rm -r src/snake/game/
rm -r src/engine/GameBase.scala src/engine/graphics/
tar cvzf snake.tar.gz *

