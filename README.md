# Tank-Trouble remastered

Tank-trouble is a merely funny game, mostly about peanuts.

![PainTank](https://github.com/leoNord462/tank_trouble_remastered/blob/main/assets/entities/tank/painTank.png)

## Entity

Base class for all visible elements in the game.

## Bullet

Class of the bullet shot by the tank.
It exists during 5 seconds from the moment it is shot.
It follows Snell-Descartes' law for collision.

## GamePanel

It is the GUI of the program.

## KeyHandler

This class handles all the inputs of our program with the KeyListener interface.

## Tank

It is the class of the tank, used by the player.
It moves with a forward/angle method.


### Update note n°1 (12/03 by arosard):

*New abstract class: MovingEntity.
It is an Extension of Entity and the new mother class of Tank and Bullet. The aim of this is to separate attributes from entity, set the methods needed by a moving entity and to "unifromize" similar attributes and methods in the classes Tank and Bullet:

 - angle/direction -> angle: to avoid confusion

 - nextY/nexX became attributes: widely used by both classes and inherent to a moving entity
 - attribute speed Entity -> MovingEntity: a power-up wouldn't have any but is an entity
 - new abstract method collision(): to segment the code of tank and bullet and because both they "do the same thing"
 - new abstract method updatePosition(): all moving entities move
 - new method updateNextPosition(): computes the nextX and nextY from attributes angle,speed,x,y

*New Class SuperTank:
abstract extension of Tank with a special ability:
 - new capacityActivated: true if the capacity is in use
 - new capacityDuration: how long does the capacity lasts
 - new capacityCooldown: how long it takes to reuse the ability
 - new cooldownEnd: time at which the capacity will be reusable
 - new durationEnd: time at which the capacity will end
 - new method capacityActiated(): if key is Pressed and cooldown is ok, capacity is initiated

*All files, but mainly Bullet and Tank:
separation of methods, essentially from the update().

### Update note n°2 (13/03 by leoNord462):

*Rectified keyReleased for a and m
*Tank needs an image to be constructed

*Changes in SuperTank:
 - Override draw() to draw progress bar
 - bettered timers (sorry Alexandre)

*Finished Phantom Tank:
 - new Phantom tank sprite
 - state variable collisionWithTiles to activate the power
 - tank is transparent while capacityActivated
