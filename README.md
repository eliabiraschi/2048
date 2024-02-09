# game2048

This is my Clojure implementation of the 2048 game.
It is a simple game where you have to move the tiles in a 4x4 grid to merge them and reach the 2048 tile.

https://en.wikipedia.org/wiki/2048_(video_game)

## Usage

Use the `awsd` keys to move the tiles in the grid.
`q` to quit the game.

You need hit `Enter` after each command and the visualization of the matrix does not take into account the width of the values so it gets wird.

Any other command is it not handled and the game will just go on.

There is no check for the game finish, so you have to stop the game when you reach the 2048 tile or when you are not able to move the tiles anymore.

### Bugs

Sometimes the tiles do not collapse as expected.
