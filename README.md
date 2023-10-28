## Game description

The goal of Snake is to create a snake as long as possible. This is achieved by guiding the
snake to an apple on the game board. The snake cannot stop moving, and dies whenever
it hits something (excluding apples). Because the snake is growing longer and longer as
the game progresses, it gets increasingly difficult to avoid collisions with the snake itself.
The player can change the direction of the head of the snake by using the arrow keys. At
step in the game, there is always an apple somewhere on the board. If the snake eats an
apple, the snake becomes one cell longer per step, for 3 steps. A new apple is placed on
a random location, excluding all places covered by the snake. When the snake reaches
an end of the game board, it re-emerges at the opposite end.

It is probably easiest to understand the game by playing it yourself. You can find a lot
of snake implementations on the web, for example [here](https://www.coolmathgames.com/0-snake). 
There are four differences between this implementation and the one you
should make. Your implementation should:
* make the snake should grow by 3 blocks instead of 4.
* make the snake re-emerge at the other end of the screen when reaching the end of
  the screen instead of dying.
* make the snake start at a length of 3, at the top left (instead of a length of 1 at
  the center).
* show the direction of the snake head.

## Framework 

Code to draw the game state and handle keyboard events has already been provided in `src/snake/game/SnakeGame.scala`, 
where you can also run the game (press the play button). You only have to implement the game logic. Your implementation
 should go in `src/snake/logic/SnakeLogic.scala` . If you want to add additional files, please make certain to
keep them in the `src.snake.logic` package (that is the directory `src/snake/logic`).

The drawing and event code, `SnakeGame` interacts with the game logic `GameLogic` as follows:

* When drawing the game, `SnakeGame` asks `GameLogic` for each cell in the grid what the type is. The cell types are 
 defined `snake.logic.CellType` and can be one of the following: 
   - `Empty()`
   - `Apple()`
   - `SnakeHead(direction: Direction)` Direction is one of `North(), East(), South()` or `West()`  
   - `SnakeBody(distanceToHead: Float)` The float indicated the color of the snake body, with 1.0 indicating dark green 
   and 0.0 indicating light green. This can be used to make the color of the head light green and the color of the body 
   turn gradually darker. It is optional to do this. 
* When the user pressed an arrow key, the method `changeDirection` is called with the direction of the arrow 
(`North(), East(), South()` or `West()`)
* When the user presses the `"r"` key, the method `setReverse` is called with argument `True`. When the user releases
 the `r` key the method is called again with the argument `False`. This is used in assignment 2.3
 * Every 5 seconds (by default) the method `step()` is called to advanced the game: move the snake, handle apple and 
 handle game over. You can adjust the speed of the game by increasing or decreasing the value `GameLogic.FramesPerSecond`.
 
 A full example which uses a similar setup is the [Sokoban example](https://gitlab.com/vu-oofp/gamebase/-/tree/sokoban).

## Assignments

To get a handle on how the framework works, please try to implement the following things first:

1. Fill the screen with `SnakeHead` triangles pointing east.
2. Same as 1 but now change the direction of the `SnakeHead` triangles when the user pressed an arrow key.
3. Same as 2. but now only draw a single `SnakeHead` triangle at position x = 5, y = 3.
4. Same as 3. but now the `SnakeHead` triangle should move, it should start in positon x = 5, y = 3 and every step move
in the direction that the triangle is pointing. If the triangle moves out of the grid it should re-emerge at the other 
end. 

There are 3 graded assignments for snake:
### 2.1 Basic snake

For this assignment implement the snake game _without_ reverse mode. This assignment is graded pass/fail. You pass the 
assignment if your implementation passes 10 tests or more. The tests can be found in `SnakeTestsAssignment2_1` or by
 running `./gradlew test2_1`.

Your implementation must NOT implement reverse mode. The idea is
that reverse mode is only implemented after the peer review such that there is some
functionality in 1.3 which is not seen by other students in the peer review of 1.2. If you
already implemented reverse mode, do not just disable reverse mode, remove any code
that deals with it. Keep your code as simple and readable as possible. You code will be
reviewed by your fellow student according to the clean code guidelines(to be published). Please read these and
try to produce code with no offenses.

If your implementation is more than 400 lines long (including empty lines), then some-
thing is probably wrong. Did you duplicate code or re-implement basic data-structures?
The reference implementation is 150 lines long. 


## 2.2 Peer review
 
 You will review and grade the snake implementations of 2.1 by 3 other students using the grading guidelines (to be published). The 
 codegrade system will assign implementations for you to grade.
For each implementation, do the following:
* Check for offenses of the guide and document them in the codegrade system
* Write a small text in codegrade describing if you thought the code was easy to read and how the code should be improved.

 
Peer reviews are graded by us using a pass/fail system. We will check if you made an
effort to review the code and find penalties. If you receive very good code, it is of course
OK to just state that it is very good and that you could not find any penalties. However,
you will certainly fail if you receive code which is obviously bad on which you did not
report a single penalty.

When reviewing the code of other students please be nice and constructive!
* Do not use swear words or strong language (e.g., do NOT use WTF).
* Do not be condescending. Say that something is unclear, not that it is “stupid”.  
* State how something can be improved, do not only criticize.

Failure to be nice and/or constructive will lead to you failing assignment 2.1.

## 2.3 Snake reverse mode 

Using the feedback from 3 other students from the peer review, you
will improve your implementation of Snake. It is up to you to decide which suggestion from your fellow students make sense, you do not have to follow up on all suggestions. It is OK to improve your code by drawing inspiration from the code your reviewed from other students, but do not copy the code
of other students! 

For assignment 2.3 you should additionally implement a reverse mode which allows the player to rewind the game to an 
earlier state.   When reverse mode is enabled, on each step the snake game returns to the previous game state. When 
reverse mode is disabled again, the game will progress as normal from the point to which you reversed.
Hence, you can rewind the game for a number of steps and then continue playing from
this state. Reverse mode should not be implemented for assignment 2.1.

The tests can be found in `SnakeTestsAssignment2_3` or by running `./gradlew test2_3`.

Grading is built up as follows:

* Amount of tests passed: 5.5 points
* Use of _immutable_ gamestate: 2 points (see below)
* Do not use a 2 dimensional array/list 0.5 (see below)
* Code style 2 points 
Total : 10 points 

An example of immutable game state can be found in the [Sokoban example](https://gitlab.com/vu-oofp/gamebase/-/tree/sokoban) 
in the [immutable sokoban logic class](https://gitlab.com/vu-oofp/gamebase/-/blob/sokoban/src/main/sokoban/logic/SokobanLogic.scala). 
To get the full 1.5 points, you need to:
* Not use mutable containers (Arrays and others)
* Use a GameState that contains only vals
* Use vars only to (these are the allowed vars, you are of course allowed to use a subset of these):
   - keep track of the current gamestate
   - keep track of past gamestates
   - keep track of whether reverse is enabled
   - keep track of the current direction
* Implement the logic for generating a new Gamestate object *inside* the gamestate object.

You are allowed to locally use vars and builders (i.e. ListBuilder) inside functions (NOTE: this is not true for the next assignment, there this is not allowed for the immutability bonus).

To get the points for not using a 2-dimensional structure:
* Do not store cells in a 2-dimensional structure
* Do not store points for empty grid cells.
This typically means storing the Snake as a list of points, and the apple as a point. For the purposes of grading a `Map[Point, CellType]` or something similar is considered a 2 dimensional structure.

The reward for using an immutable gamestate and not using a 2 dimensional array/list is to reward trying out new styles
of programming. Code style is judged as described in the readable code lectures and the
 [code style grading guideline](https://canvas.vu.nl/courses/50305/pages/code-style). Note: If you do not implement reverse mode, you get a 0.6 point style penalty.

## Test setup

To make sure that everyone programs exactly the same thing, we have implemented
a number of tests that your Snake implementation should pass. This section describes
these tests and the specific requirement on your snake implementation.

Each test describes an expected run of a snake game, typically on a small board (for
example, 6x3). A run consists of multiple *frames* (between each frame step is called).
For each frame, the test lists:
* The next “random” number (this is important for reproducible apple placement).
* A list of actions that the player performed before this frame, namely changing directions (and later, using the reverse button).
* The placement of the snake and the food.
Each test also contains a "Hint": a brief description of what is being tested

As an example, consider the following frame as it would be displayed to you from IntelliJ:
```
step=4, rand=2, actions=<ChangeDir(West()), ChangeDir(North()), Step>
Want
....
^A..
OO..
....
```
This indicates:
* The total number steps is 4
* The next random number is 2
* The player pressed west, and then north, before the screen was rendered.
* The game field is 4x4 cells big. 
* The snake is heading north, with the head at row
 (counting from 0) and column 0, and the body at row 2, col 0, and row 2, col 1.
The apple is currently at row 1, col 1.

The following actions can be encountered:
```
+-------------------------------|--------------------------------+
| Action                        | Meaning                        |
+-------------------------------+--------------------------------|
| ChangeDir(dir : Direction)    | Change snake direction         |
| ReverseGame(enable : Boolean) | Disable or enable reverse mode |
+----------------------------------------------------------------+
```
Direction is one of North(), East(), South() or West() . Reverse mode must NOT be
implemented for assignment 2.1 (it must be implemented for assignment 2.3).

The ASCII display of the game board should read as follows:
```
+-----------+----------------------------+
| Character | Meaning                    |
+-----------+----------------------------|
| "O"       | Snake body                 |
| "^"       | Snake Head heading North   |
| "v"       | Snake Head heading South   |
| ">"       | Snake Head heading East    |
| "<"       | Snake Head heading West    |
| "."       | Empty grid cell            |
| "A"       | Food                       |
+----------------------------------------+
```

If a test fails, you will see the frames as we expected them and the ones you implemen-
tation produced. For example, if you would fail testChangeDirs , then you might see
this:
```
org.scalatest.exceptions.TestFailedException: didPass was false
Hint: You can change direction.
--------------------------------------------------------------------------------

step=0, rand=2, actions=<>
Want   | Got ✓ 
---------------
OO>.   | OO>.
.A..   | .A..
....   | ....
....   | ....

step=4, rand=2, actions=<ChangeDir(South(), Step)>
Want   | Got ✗ 
---------------------------
.OO.   | .OO>
.Av.   | .A..
....   | ....
....   | ....
```
On each frame the expected output (Want) and the output produced by your implementation 
are displayed side-by-side. A checkmark (✓) indicates that on this frame the expected ouput and the
output produced by your implementation are the same, a cross (✗) indicates that they differ.
 Here, the implementation seems to ignore a change in direction.
 
 ## Apple placement
 
 To ensure reproducible behavior, we specify exactly how a random number generator must be used to place the food. The random number generator itself is passed
 to the SnakeLogic class as an argument called `randomGen: RandomGenerator` . This
 `RandomGenerator` class has the following method:
 ```scala 
 def randomInt(upTo: Int): Int
```
This gives a random integer in the range [0 − upTo ) (exclusive). During testing, a dummy
integer generator will be passed to SnakeLogic which generates the numbers specified
in the test frames.

The apple must be placed randomly at a free spot on the board. You might be tempted
to randomly place the apple using the following method, which we DO NOT USE:
1. Pick a random horizontal coordinate.
2. Pick a random vertical coordinate.
3. If the resulting coordinate is not free, try again.

This method has the downside that it might take a lot of tries before you find a free spot,
especially if the board is almost full. Moreover, it requires arbitrary amount of numbers
for placing a single apple, which complicates testing.

Instead of the above method, we opt for a method which always succeeds at placing an
apple, using only a single number. To see how this works, suppose we have a 5x3 board
and the current position of the snake is as follows:
```
.....
.OO>.
.....
```
To determine the position of the apple, we want to pick a random free spot on the board
(the apple should not be placed on the snake). We number the free spots on the board
from left to right, top to bottom, skipping any spot occupied to by the snake. In our example,
this means the following numbering:
```
 0 | 1 | 2 | 3 | 4 
---+---+---+---+---
 5 | X | X | X | 6 
---+---+---+---+---
 7 | 8 | 9 | 10| 11   
```
To pick a random spot, first compute the number of free spots and then call `randomGen.randomInt(nrFreeSpots)`
(in our case nrFreeSpots=12 ). Then  place the apple at the position corresponding to the
number you got.

