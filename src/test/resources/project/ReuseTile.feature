Feature: Test features for reusing tiles from table
  Scenario Outline: Player 1 reuses tiles to form new melds
    Given Player 1 has tiles: <preset1>
    And Player 2 has tiles: <preset2>
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays <preset2> melds
    And Player 3 draws a tile
    When Player 1 reuses <tiles> from meld 2 to play <melds2>
    Then Player 1 has <number> tiles
    And Table has melds <list>
    Examples:
      | preset1             | preset2                  | tiles  |melds2       | number | list                                     |
      | R10 R11 R12 R13 G13 | B10 B11 B12 B13          | B13    |R13 G13 B13  | 9      | {B10 B11 B12},{*R13 *G13 !B13}           |
      | R10 R11 R12 B11     | B9 B10 B11 B12 B13       | B9 B10 |B9 B10 B11   | 10     | {B11 B12 B13},{!B9 !B10 *B11}            |
      | R10 R11 R12 G10 O10 | B7 B8 B9 B10 B11 B12 B13 | B10    |B10 G10 O10  | 9      | {*G10 !B10 *O10},{B7 B8 B9},{B11 B12 B13}|

  Scenario: Player 1 reuses a tile to add tiles from hand
    Given Player 1 has tiles: R10 R11 R12 R1
    And Player 2 has tiles: B10 B11 B12
    And Player 3 has tiles: R13 G13 B13 O13
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays B10 B11 B12 melds
    And Player 3 plays R13 G13 B13 O13 melds
    When Player 1 reuses R13 from meld 3 to add R13 R1 to meld 1
    Then  Player 1 has 10 tiles
    And Table has melds {R10 R11 R12 !R13 *R1},{G13 B13 O13}

  Scenario: Player 1 reuses a tile not in meld
    Given Player 1 has tiles: R10 R11 R12 G10 O10
    And Player 2 has tiles: B6 B7 B8 B9
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays B6 B7 B8 B9 melds
    And Player 3 draws a tile
    When Player 1 reuses B10 from meld 2 to play G10 B10 O10
    Then Player 1 receives an penalty for invalid move
    And Player 1 has 14 tiles

  Scenario: Player 1 reuses tiles but not plays all reused tiles
    Given Player 1 has tiles: R10 R11 R12 G10 O10
    And Player 2 has tiles: B6 B7 B8 B9 B10
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays B6 B7 B8 B9 B10 melds
    And Player 3 draws a tile
    When Player 1 reuses B6 B10 from meld 2 to play G10 B10 O10
    Then Player 1 receives an penalty for invalid move
    And Player 1 has 14 tiles

  Scenario Outline: Player 1 performs invalid reuse moves reuses tiles making some melds invalid in the table
    Given Player 1 has tiles: R10 R11 R12 G10 O10
    And Player 2 has tiles: <preset2>
    And Players start the game
    And Player 1 plays R10 R11 R12 melds
    And Player 2 plays <preset2> melds
    And Player 3 draws a tile
    When Player 1 reuses <tiles> from meld 2 to play G10 B10 O10
    Then Player 1 receives an penalty for invalid move
    And Player 1 has 14 tiles

    Examples:
      | preset2         | tiles   |
      | B6 B7 B8 B9     | B10     |
      | B6 B7 B8 B9 B10 | B6 B10  |
      | B8 B9 B10 B11   | B10     |
      | B7 B8 B9 B10    | B10 B10 |

