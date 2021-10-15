package project;

import java.io.ByteArrayInputStream;

import io.cucumber.java8.En;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefs implements En {
    LocalGame game;
    String tile1;
    String tile2;
    String tile3;
    String output;

    public StepDefs() {
        game = new LocalGame();
        StringBuilder inString = new StringBuilder();
        tile1 = "";
        tile2 = "";
        tile3 = "";

        Given("^Players start the game$", () -> {
            game.reset(tile1, tile2, tile3);
        });

        When("^Player (\\d+) draws a tile$", (Integer arg0) -> {
            inString.append("draw").append(System.lineSeparator());
        });

        When("^Player (\\d+) enters wrong action$", (Integer arg0) -> {
            inString.append("end").append(System.lineSeparator());
        });

        Then("^Game shows result$", () -> {
            game.setInput(inString.toString());
            inString.setLength(0);
            game.start();
            output = game.getOutput();
        });

        Then("^Player (\\d+) has (\\d+) tiles$", (Integer arg0, Integer arg1) -> {
            assertEquals(arg1, game.getHand(arg0 - 1).size());
        });

        Then("^Next Player is Player (\\d+)$", (Integer arg0) -> {
            assertEquals(arg0, game.getCurrentPlayer() + 1);
        });

        Then("^Player receives an error message$", () -> {
            assertEquals("Error found, try again", game.error());
        });


    }
}
