Feature: Test features for drawing tiles and player sequence
  Scenario: Players draw a tile in sequence
    Given Players start the game
    And Player 1 draws a tile
    And Player 2 draws a tile
    And Player 3 draws a tile
    When Player 1 draws a tile as a try
    Then Player 1 has 16 tiles
    And Next Player is Player 2

  Scenario: Players draw a tile in sequence, Player 3 enters wrong action
    Given Players start the game
    And Player 1 draws a tile
    And Player 2 draws a tile
    When Player 3 enters wrong action
    Then Player receives an error message
    And Next Player is Player 3
