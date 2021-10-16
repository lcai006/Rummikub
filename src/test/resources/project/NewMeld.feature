Feature: Test features for players' basic play with playing new melds.
  Initial melds should have at least 30 points
  Scenario Outline: Player 1 plays <meld> with at least 30 points for initial hand
    Given Player 1 has tiles: <tiles>
    And Players start the game
    When Player 1 plays <melds>
    Then Game shows result
    And Player 1 has <number> tiles
    And Table has melds <list>
    Examples:
      | tiles             | melds             | number | list                       |
      | R10 G10 B10       | R10 G10 B10       | 11     | {*R10 *G10 *B10}           |
      | R10 R11 R12 R13   | R10 R11 R12 R13   | 10     | {*R10 *R11 *R12 *R13}      |
      | R5 G5 B5 O7 O8 O9 | R5 G5 B5,O7 O8 O9 | 8      | {*R5 *G5 *B5},{*O7 *O8 *O9}|

  Scenario: Player 1 plays a meld with an unrecognizable tile
    Given Player 1 has tiles: R10 R11 R12
    And Players start the game
    When Player 1 plays R10 R11 A15
    Then Game shows result
    And Player receives an error message
    And Next Player is Player 1

  Scenario: Player 1 plays a meld with a tile not in hand
    Given Player 1 has tiles: R10 R11 R12
    And Players start the game
    When Player 1 plays B10 B11 B12
    Then Game shows result
    And Player 1 receives an penalty for invalid move
    And Player 1 has 17 tiles

  Scenario: Player 1 plays an invalid meld
    Given Player 1 has tiles: B10 B12 R13
    And Players start the game
    When Player 1 plays B10 B12 R13
    Then Game shows result
    And Player 1 receives an penalty for invalid move
    And Player 1 has 17 tiles

  Scenario: Player 1 plays several melds with an invalid meld
    Given Player 1 has tiles: R1 R2 R3 B10 B12 R13
    And Players start the game
    When Player 1 plays R1 R2 R3,B10 B12 R13
    Then Game shows result
    And Player 1 receives an penalty for invalid move
    And Player 1 has 17 tiles

  Scenario: Player 1 plays initial melds with less than 30 points
    Given Player 1 has tiles: R1 R2 R3 B4 G4 O4
    And Players start the game
    When Player 1 plays R1 R2 R3,B4 G4 O4
    Then Game shows result
    And Player 1 receives an penalty for invalid initial play
    And Player 1 has 17 tiles

  Scenario: Player 1 plays melds after initial play
    Given Player 1 has tiles: R10 R11 R12 B3 G3 O3 B4 B5 B6
    And Players start the game
    And Player 1 plays R10 R11 R12
    And Player 2 draws a tile
    And Player 3 draws a tile
    When Player 1 plays B3 G3 O3,B4 B5 B6
    Then Game shows result
    And Player 1 has 5 tiles
    And Table has melds {*G3 *B3 *O3},{*B4 *B5 *B6}