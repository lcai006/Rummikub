package project;

import io.cucumber.java8.En;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicStepDefs implements En {
    LocalGame game;
    String tile1;
    String tile2;
    String tile3;
    String output;

    public BasicStepDefs() {
        game = new LocalGame();
        StringBuilder inString = new StringBuilder();
        tile1 = "";
        tile2 = "";
        tile3 = "";

        Given("^Players start the game$", () -> game.reset(tile1, tile2, tile3));

        When("^Player (\\d+) draws a tile( as a try)?$", (Integer arg0, String trigger) -> {
            inString.append("draw").append(System.lineSeparator());
            if (trigger != null) {
                game.setInput(inString.toString());
                inString.setLength(0);
                game.start();
            }
         });

        When("^Player (\\d+) enters wrong action$", (Integer arg0) ->{
            inString.append("end").append(System.lineSeparator());
            game.setInput(inString.toString());
            inString.setLength(0);
            game.start();
        });

        Then("^Player (\\d+) has (\\d+) tiles$", (Integer arg0, Integer arg1) ->
            assertEquals(arg1, game.getHand(arg0 - 1).size()));

        Then("^Next Player is Player (\\d+)$", (Integer arg0) ->
            assertEquals(arg0, game.getCurrentPlayer() + 1));

        Then("^Player receives an error message$", () ->
            assertEquals("Error found, try again", game.error()));

        Given("^Player (\\d+) has tiles: (.+)$", (Integer arg0, String tiles) -> {
            switch (arg0) {
                case 1:
                    tile1 = tiles;
                case 2:
                    tile2 = tiles;
                case 3:
                    tile3 = tiles;
            }
        });

        When("^Player (\\d+) plays (.+) melds( as a try)?$", (Integer arg0, String melds, String trigger) -> {
            for(String meld: melds.split(",")) {
                inString.append("new ").append(meld).append(System.lineSeparator());
            }
            inString.append("end").append(System.lineSeparator());
            if (trigger != null) {
                game.setInput(inString.toString());
                inString.setLength(0);
                game.start();
            }
        });

        Then("^Table has melds (.+)$", (String melds) -> {
            for(String meld: melds.split(",")) {
                assertTrue(game.getTable().toString().contains(meld));
            }
        });

        Then("^Player (\\d+) receives an penalty for invalid move$", (Integer arg0) -> assertEquals("Invalid Move, Player 1 draws 3 tiles", game.error()));

        Then("^Player (\\d+) receives an penalty for invalid initial play$", (Integer arg0) -> assertEquals("Need 30 points for initial play", game.error()));

        When("^Player (\\d+) adds (.+) to meld (\\d+)( as a try)?$", (Integer arg0, String tiles, Integer meldId, String trigger) -> {
            inString.append("add ").append(meldId).append(" ").append(tiles).append(System.lineSeparator());
            inString.append("end").append(System.lineSeparator());
            if (trigger != null) {
                game.setInput(inString.toString());
                inString.setLength(0);
                game.start();
            }
        });

        Then("^Player (\\d+) wins the game$", (Integer arg0) -> assertEquals(arg0 - 1, game.getWinner()));

        Then("^Player 1 has (-?\\d+) points, player 2 has (-?\\d+) points, player 3 has (-?\\d+) points$", (Integer arg0, Integer arg1, Integer arg2) -> {
            assertEquals(arg0, game.getScores(0));
            assertEquals(arg1, game.getScores(1));
            assertEquals(arg2, game.getScores(2));
        });

        And("^Player (\\d+) reuses (.+) from meld (\\d+)$", (Integer arg0, String tiles1, Integer meldId) -> inString.append("reuse ").append(meldId).append(" ").append(tiles1).append(System.lineSeparator()));
        
        When("^Player (\\d+) reuses (.+) from meld (\\d+) to play (.+)$", (Integer arg0, String tiles1, Integer meldId, String tiles2) -> {
            inString.append("reuse ").append(meldId).append(" ").append(tiles1).append(System.lineSeparator());
            inString.append("new ").append(tiles2).append(System.lineSeparator());
            inString.append("end").append(System.lineSeparator());

            game.setInput(inString.toString());
            inString.setLength(0);
            game.start();
        });

        When("^Player (\\d+) reuses (.+) from meld (\\d+) to add (.+) to meld (\\d+)$", (Integer arg0, String tiles1, Integer meldId1, String tiles2, Integer meldId2) -> {
            inString.append("reuse ").append(meldId1).append(" ").append(tiles1).append(System.lineSeparator());
            inString.append("add ").append(meldId2).append(" ").append(tiles2).append(System.lineSeparator());
            inString.append("end").append(System.lineSeparator());

            game.setInput(inString.toString());
            inString.setLength(0);
            game.start();
        });

    }
}
