Feature: Test features for adding tiles from hand to melds in the table
  Scenario Outline: Player 1 adds tiles from hand to a meld in the table
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
      | preset1             | preset2     | melds2      | tiles | number | list                  |
      | R10 R11 R12 B13     | B10 B11 B12 | B10 B11 B12 | B13   | 10     | {B10 B11 B12 *B13}    |
      | R10 R10 R11 R12     | B10 G10 O10 | B10 G10 O10 | R10   | 10     | {*R10 G10 B10 O10}    |
      | R10 R11 R12 G9 G13  | G10 G11 G12 | G10 G11 G12 | G9 G13| 9      | {*G9 G10 G11 G12 *G13}|

  Scenario: Player 2 performs add action before initial play
    Given Player 1 has tiles: R10 R11 R12
    And Player 2 has tiles: R13
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    When Player 2 adds R13 to meld 1 as a try
    Then Player 2 receives an penalty for invalid initial play
    And Player 2 has 17 tiles

  Scenario: Player 1 adds a tile which is not in hand to a meld
    Given Player 1 has tiles: R1 R1 R2 R2 R3 R3 R4 R4 R5 R5 R6 R10 R11 R12
    And Player 2 has tiles: B10 B11 B12
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays B10 B11 B12 melds
    And Player 3 draws a tile
    When Player 1 adds B9 to meld 2 as a try
    Then Player 1 receives an penalty for invalid move
    And Player 1 has 14 tiles

  Scenario Outline: Player 1 add tiles to a meld but that meld becomes invalid
    Given Player 1 has tiles: <preset>
    And Players start the game
    And Player 1 plays <melds> melds
    And Player 2 draws a tile
    And Player 3 draws a tile
    When Player 1 adds <tiles> to meld 1 as a try
    Then Player 1 receives an penalty for invalid move
    And Player 1 has <number> tiles

    Examples:
      | preset              | melds           | tiles | number |
      | R1 R7 R11 R12 R13   | R11 R12 R13     | R1 R7 | 14     |
      | R10 R10 G10 B10 O10 | R10 G10 B10 O10 | R10   | 13     |



