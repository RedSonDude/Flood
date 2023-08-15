Quick Demo
----------

https://github.com/rsrinivasan1/Flood/assets/52140136/2659f51d-2fe7-4f86-9ef9-827320d20acc

About
-----
Flood is a top-down racing style game created using Java. There are three levels: the walls in the first level slow the player down slightly; the walls in the second level slow the player down significantly; the walls in the third level bounce the player backwards into certain death.

I was able to develop a basic framework for the game by following this tutorial by CodeNMore: https://www.youtube.com/watch?v=dEKs-3GhVKQ&list=PLah6faXAgguMnTBs3JnEJY0shAc18XYQZ

I then added all the necessary elements into my game independently, using various resources to learn as I went. Making a game in Java was not easy, but this is what made this game so rewarding for me to build.

Releases
--------
If you would like to download the game, look for the newest release on the releases page and download the Flood.jar file: https://github.com/rsrinivasan1/Flood/releases  

Make sure you have the Java Runtime Environment before trying to launch.

Features
--------

Flood boasts a never-ending, randomly generated Bezier curve track that continues as long as the player survives. As one curve in the track comes to an end, a tangent curve is immediately generated such that the path is seamlessly continued without interruption to the game.

All graphics were created by using the Java Abstract Window Toolkit, in particular making use of the Graphics2D class to render the player object and the scene.

Collision detection and handling is implemented by estimating each Bezier curve as a sequence of small, connected line segments. This was done because it was easier to check for collisions between a bounding box of lines and an ```Area``` object, than it would have been to do the same for a Bezier curve bounding box.

Finally, high scores are saved locally and encrypted using SHA-256 in order to prevent external manipulation of score data.
