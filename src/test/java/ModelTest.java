import model.Deck;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @DisplayName("test the deck creation")
    @Test
    public void testDeck() {
        Deck deck = new Deck();
        assertEquals(104, deck.size());
    }
}
