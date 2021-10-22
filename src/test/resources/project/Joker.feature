Feature: Test features for playing with joker
  Scenario Outline: Player 1 plays tiles with joker for initial hand
    Given Player 1 has tiles: <tiles>
    And Players start the game
    When Player 1 plays <melds> melds as a try
    Then Player 1 has <number> tiles
    And Table has melds <list>
    Examples:
      | tiles                | melds                | number | list                       |
      | Joker G10 B10        | Joker G10 B10        | 11     | {*G10 *B10 *Joker}         |
      | R8 G8 B8 Joker       | R8 G8 B8 Joker       | 10     | {*R8 *G8 *B8 *Joker}       |
      | Joker R12 R13        | Joker R12 R13        | 11     | {*Joker *R12 *R13}         |
      | R9 Joker R11 R12     | R9 Joker R11 R12     | 10     | {*R9 *Joker *R11 *R12}     |
      | R11 R12 R13 Joker R2 | R11 R12 R13 Joker R2 | 9      | {*R11 *R12 *R13 *Joker *R2}|

  Scenario: Player 1 plays initial melds containing joker with less than 30 points
    Given Player 1 has tiles: R7 R8 Joker
    And Players start the game
    When Player 1 plays R7 R8 Joker melds as a try
    Then Player 1 receives an penalty for invalid initial play
    And Player 1 has 17 tiles

  Scenario: Player 1 plays initial melds containing joker with less than 30 points
    Given Player 1 has tiles: R12 R13 Joker
    And Players start the game
    When Player 1 plays R12 R13 Joker melds as a try
    Then Player 1 receives an penalty for invalid initial play
    And Player 1 has 17 tiles

  Scenario: Player 1 plays an invalid meld with joker
    Given Player 1 has tiles: R7 R12 Joker
    And Players start the game
    When Player 1 plays R7 R12 Joker melds as a try
    Then Player 1 receives an penalty for invalid initial play
    And Player 1 has 17 tiles

  Scenario Outline: Player 1 adds tiles with joker from hand to a meld in the table
    Given Player 1 has tiles: <preset1>
    And Player 2 has tiles: <preset2>
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays <melds2> melds
    And Player 3 draws a tile
    When Player 1 adds <tiles> to meld 2 as a try
    Then Player 1 has <number> tiles
    And Table has melds <list>
    Examples:
      | preset1             | preset2     | melds2      | tiles        | number | list                     |
      | R10 R11 R12 Joker   | B10 B11 B12 | B10 B11 B12 | Joker=B9     | 10     | {*Joker B10 B11 B12}     |
      | R10 R11 R12 Joker B9| B10 B11 B12 | B10 B11 B12 | B9 Joker=B13 | 9      | {*B9 B10 B11 B12 *Joker} |
      | R10 R11 R12 Joker   | B10 G10 O10 | B10 G10 O10 | Joker        | 10     | {*Joker G10 B10 O10}     |

  Scenario: Player 1 add tiles with joker to a meld but that meld becomes invalid
    Given Player 1 has tiles: R10 R11 R12 R7 Joker
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 draws a tile
    And Player 3 draws a tile
    When Player 1 adds R7 Joker=R8 to meld 1 as a try
    Then Player 1 receives an penalty for invalid move
    And Player 1 has 14 tiles

  Scenario Outline: Player 1 reuses a joker to form new melds
    Given Player 1 has tiles: R10 R11 R12 R2 R3 B10
    And Player 2 has tiles: <preset>
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays <preset> melds
    And Player 3 draws a tile
    When Player 1 reuses Joker with B10 from meld 2 to play R2 R3 Joker
    Then Player 1 has <number> tiles
    And Table has melds <list>

    Examples:
      | preset            | number | list                               |
      | Joker B11 B12     | 8      | {*B10 B11 B12},{*R2 *R3 !Joker}    |
      | R10 G10 Joker     | 8      | {R10 G10 *B10},{*R2 *R3 !Joker}    |
      | R10 G10 Joker     | 8      | {R10 G10 *B10},{*R2 *R3 !Joker}    |
      | R10 G10 O10 Joker | 8      | {R10 G10 *B10 O10},{*R2 *R3 !Joker}|

  Scenario Outline: Player 1 reuses joker incorrectly
    Given Player 1 has tiles: <preset1>
    And Player 2 has tiles: <preset2>
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays <preset2> melds
    And Player 3 draws a tile
    When Player 1 reuses <replace> from meld 2 to play R2 R3 Joker
    Then Player 1 receives an penalty for invalid move
    And Player 1 has <number> tiles

    Examples:
      | preset1               | preset2       | replace        | number |
      | R10 R11 R12 R2 R3     | Joker B11 B12 | Joker          | 14     |
      | R10 R11 R12 R2 R3 B11 | Joker R10 G10 | Joker with B11 | 14     |
      | R10 R11 R12 R2 R3     | Joker B11 B12 | Joker with B10 | 14     |

  Scenario: Player 1 reuses joker with a tile from table
    Given Player 1 has tiles: R10 R11 R12 R13 R2 R3
    And Player 2 has tiles: Joker B10 G10
    And Players start the game
    And Player 1 plays R10 R11 R12 R13 melds
    And Player 2 plays Joker B10 G10 melds
    And Player 3 draws a tile
    And Player 1 reuses R10 from meld 2
    When Player 1 reuses Joker with R10 from meld 2 to play R2 R3 Joker
    Then Player 1 receives an penalty for invalid move
    And Player 1 has 13 tiles

  Scenario: Player 1 reuses a tile from a meld containing joker
    Given Player 1 has tiles: R10 R11 R12 R13 B8 B9
    And Player 2 has tiles: B10 B11 B12 Joker
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays B10 B11 B12 Joker melds
    And Player 3 draws a tile
    When Player 1 reuses B10 from meld 2 to play B8 B9 B10
    Then Player 1 receives an penalty for invalid move
    And Player 1 has 14 tiles

  Scenario: Player 1 reuses joker with a tile from table
    Given Player 1 has tiles: R10 R11 R12 R2 R3 B10
    And Player 2 has tiles: Joker R10 G10
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays Joker R10 G10 melds
    And Player 3 draws a tile
    When Player 1 reuses Joker with B10 from meld 2 to add Joker=R13 to meld 1
    Then Player 1 receives an penalty for invalid move
    And Player 1 has 14 tiles
