General
- Random number is generated using the Java Random library without using the seed.


Items
- Equipment can only be sold at the hero's castle
- If trance is triggered on an enemy, that enemy will be attacked by the enemies that are not in trance.
- If an enemy is transformed to allied soldier by staff, the enemy will no longer take damage from the allied side in that round, but all the previous damage in this round will take effect.
- A staff can only trance real enemies, it cannot trance a allied soldier that has been turned to zombie.
- Enemies turned into allied side will remain their previous states for health, attack and defense.
- Occasions of which equipment can be obtained:
```
+-------------------+-----------------+
| Defeat a vampire  |                 |
| Defeat a zombie   |                 |
| Defeat a slug     | Receive a sword |  
+-------------------+-----------------+
```
- Rare item types will not be available in a game if it is not added to the world configuration file. 


Enemies
> 1 tile in a unit length in the gridpane
- Enemies will spawn only at the start of each cycle. Other specific building rules apply.
- Except for the slug, all other enemies move anti-clockwise.
- Vampires will run back in the opposite direction until the campfire is not in its support radius when there is a campfire.
- E.g. Since the vampire is moving in an anti-clock direction, if the vampire meets the campfire, then the vampire will run in a clockwise direction.
- If a zombie triggers a critical attack on an allied soldier, no additional damage by the zombie will be done on that soldier.
- An allied soldier turned into a zombie will not be attacked by the enemies.
Enemies will spawn after the first tick move.


Buildings
- The character can have a maximum of cards corresponding to the length of the game map.
- Enemies will spawn from the building only at the start of each cycle.
- After buildings have been put down, they cannot be moved or removed.
- Tower will join the battle on the same side with the character.
- Tower will attack each one of the enemies every round when the character does his attack in a battle.
- Tower will not take any damage.
- Tower has a 3-tiles support radius.
- Campfire has a 3-tiles support radius. 
- A trap can only damage one random enemy given there are multiple enemies stepping on the trap at the same time.
- The enemy stepping on a trap will receive 5 damage. If the enemy loses all its health, it will be killed and removed.
- Assume the buying system at hero’s castle
  - The character can purchase armour, shields, helmet, weapons and health potion with gold.
  - Character cannot purchase new item when inventory is full
  - Characters can sell existing items for gold.
- Assume that when an enemy is spawned from a building on a non-path tile, it will always appear on the closest path tile in the four directions in this order: above, below, left, right. That is, if there is no path tile above the building, the enemy will start from the path tile below the building, and so on.
- If an enemy spawning building is next to several path tiles, the enemy will spawn on the tiles depending on the precedence (Up > Right > Down > Left). I.e. If there are path tiles on the top and the right, the enemy will spawn on the top path tile.
- Buildings cannot be placed on top of each other
- Occasions of which building cards can be obtained:
```
+---------------------+--------------------------------+
| Defeat a vampire    | Receive a trap card            |
| Defeat a zombie     | Receive a village card         |
| Defeat a slug       | Receive a zombie pit card      |
|                     | Receive a vampire castle card  |
+---------------------+--------------------------------+
```


Battle
- Assume that battle happens at where the character is when entering the enemies battle radius. (Location)
- Assume that supporting enemies will go to where the character is when a battle happens.
- Assume health potion cannot be taken during a battle, it can only be taken before or after a battle.
- Assume during a battle, all other enemies on the map stop their movement.
- The attack dealt on each other depends on the attack and defense. Directly subtract the health by (opponent attack - this entity’s defense) given there is no equipment equipped. If there are equipment, perform changes based on each item's attribute.
- Battle
  - Order of adding entities to a battle
  - The character
  - The allied soldiers
  - The towers
  - The enemies
- Battle Sequencing
  - In each round, according to the orders they are added to the battle, each entity will attack all other entities that are on the opposite side.
  - Every entity will get a chance to attack every round, even if they are already defeated before their chance
  - At the end of each round, defeated entities will be removed from the list of entity that are in the battle
  - The battle finishes when either the character dies or there is no more enemy side entity in the battle (Allied soldiers can be turned in the enemy side).
- If an enemy is turned into an allied soldier by staff, or an allied soldier is turned into a zombie, their position in the list doesn’t change.
- Battle radius and support radius are from tile to tile.


Health
- Assume that the character will have full health up to his maximum health limit (100/100) and 0 health potion at the start of every game.
- Assume that the game will end when the character loses all his health (when health bar reaches 0/100), despite how many health potions he has.
- Assume that the human player is able to store health potions he collects or purchases to consume in the future.
- Assume the human player may consume any number of health potions before or after a battle but not during a battle. 
- Assume the human player can consume potions even when the health bar is full, but the health bar will be capped at 100 percentage.
- Assume the character will restore health fully up to his maximum health limit each time he consumes a health potion.
- Assume the health bar is placed on the top right side of the game interface. 
- Assume a health potion icon is placed next to the health bar with a number on the bottom right of the icon indicating the number of health potions the player has.
- Assume there is no limit on how many health potions a player can store, and the health potions are not stored in the equipment tool box.
- Assume the character obtains 1 health potion every time he passes through a health potion icon on the map.
- Assume 1 or none health potion tile will spawn randomly on path tiles for each loop the character takes, and will disappear after the character walks past.
- Assume the human player may click on the health potion icon to consume a health potion.
- Assume the health bar will update in real time of any attack in a battle.


Gold
- Assume that the human player has 0 gold at the start of every new game.
- Assume that the player can have an unlimited amount of gold.
- Assume gold will only be calculated and added after the character has defeated all enemies in a battle. 
- Assume 1 gold tile will spawn randomly on path tiles for each loop the character takes.
- Occasions of which gold can be obtained/spent:
```
   +--------------------------+-------------------------------------------------+
   | Each card destroyed      | Get 30 gold                                     |
   | Each equipment destroyed | Get 40% of original price, 100 gold if no price |
   | Health potion            | Price: 150 gold                                 |
   | Sword                    | Price: 300 gold                                 |
   | Stake                    | Price: 400 gold                                 |
   | Armour                   | Price: 600 gold                                 |
   | Shield                   | Price: 450 gold                                 |
   | Helmet                   | Price: 350 gold                                 |
   | Staff                    | Price: 450 gold                                 |
   | Walk through gold tile   | Get 75 gold                                     |
   | Defeat a vampire         | Get 80 gold                                     |
   | Defeat a zombie          | Get 51 gold                                     |
   | Defeat a slug            | Get 27 gold                                     |
   +--------------------------+-------------------------------------------------+
```


Experience
- Assume that the human player has 0 experience at the start of every new game.
- Assume that the player can have an unlimited amount of experience.
- Assume experience will only be calculated and added after the character has defeated all enemies in a battle. 
- Occasions of which experience can be obtained:
```
   +--------------------------------+-------------------------------+
   | Each card destroyed            | Get 50 XP                     |
   | Each equipment destroyed       | Get 200 XP                    |
   | Defeat all enemies in a battle | Get 0 ~ 10% of character’s XP |
   +--------------------------------+-------------------------------+
```
- Assume the first enemy defeated offers 100 XP
