# PACMAN-2-PLAYERS
## Context:
It’s as part of a school project (in duo) of 2nd year of engineering school that we programmed a Pac-Man playable with 2 players on a same computer. The objective was to use animations on JavaFx. We also made some changes to the classic gameplay.

## Gameplay:
It uses the same gameplay as the classic Pac-Man with some differences. You would pay attention to collisions between players which cause loss of life, to bonuses which does not necessarily transform players into berserker mode, but which can also give an extra life or a score bonus. The different exits of the map don’t necessarily lead to the opposite exit: it’s random.

## Available:
If you wish to view or modify the source code it is available under the folder name "PACMAN2P". The game was coded in JavaFx on IntelliJ. If you only want to play, you just need to download the "PACMAN2P The Game" in release section.

## Features:
1. You have the option to customize the map.
2. You can download skins from the internet to customize your game (.gif for the classic player and .png for the berserker player and enemies). The names of the images must remain the same as the original.
3. In the main menu, you can choose to play with 1 or 2 players as well as the number of enemies.

<p align="center">
  <img alt="pac_man_menu" src="https://user-images.githubusercontent.com/73184884/192123843-b5b3cce5-a8f1-4b0f-ba63-b6feb8c8cc20.jpg" width="720"/>
</p>

## Rule for creating a Map:
To create your map, you must create a text file which will have the same name of the file containing the basic map: "PACMAN_MAP.txt". I advise you to keep the basic map either under another name or in another folder so you can recover it at any time. When you start to compose your map, you will have to respect the following code:
- Put a "0" for enemies spawns (compulsory),
- Put a "1" for walls,
- Put a "2" for paths containing a score ball,
- Put a "3" for empty paths,
- Put a "4" for the paths containing a bonus.

<p align="center">
  <img alt="pac_man_game" src="https://user-images.githubusercontent.com/73184884/192123827-243a5062-9ed4-4ebb-9f9f-fc59af1f0384.jpg" width="720"/>
</p>

## Bonuses:
There will be 3 types of bonus which will be generated randomly according to predefined probabilities:
- Berserk mode: 3/5.
- An extra life: 1/5.
- 200 additional score points: 1/5.

## State:
- [ ] Work in progress
- [x] Work completed
