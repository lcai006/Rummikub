Feature: Test features for a player wins the game
  Scenario: Player 1 plays all tiles from hand and wins the game
    Given Player 1 has tiles: R6 R7 R8 R9 R10 R11 R12 R13 B5 B6 B7 B8 B9 B10
    And Player 2 has tiles: R1 R2 R3 R4 R5 B1 B2 B3 B4 B5 G1 G2 G3 G4
    And Player 3 has tiles: R1 R2 R3 R4 R5 B1 B2 B3 B4 G1 O1 O1 O2 Joker
    And Players start the game
    When Player 1 plays R6 R7 R8 R9 R10 R11 R12 R13,B5 B6 B7 B8 B9 B10
    Then Game shows result
    And Player 1 wins the game
    And Player 1 has 0 points, player 2 has -40 points, player 3 has -60 points




