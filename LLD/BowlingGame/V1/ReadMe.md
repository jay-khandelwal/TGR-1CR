Questions:
- Do i need to make a multi lane bowling game??
- Do we need to insert custom rounds, bowling per round??
- Can have diffrent score calculation rule??
- can have diffrent end round Rule??
- in one round, single person can have multiple channces????


Entities:
- Game
- Lane
- Player
- Round
- Turn
- Chances
- Roll



Class Game:
    - lanes: List<Lane>

Class Lane:
    - players: List<Player>

Class Player:
    - turns: List<Turn>

Class Round:
    - turns: List<Turn> = Each player has one turn

Class Turn:
    - chances: List<Chance>: idealy 1 per round per can be more, like in case of last round

Class Chance:
    - rolls: List<Roll>; idealy 2 per turn 

Class Roll:
    - knocked count: int