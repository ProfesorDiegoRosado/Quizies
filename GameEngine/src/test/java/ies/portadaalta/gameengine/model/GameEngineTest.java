package ies.portadaalta.gameengine.model;


import ies.portadaalta.TestUtils;
import ies.portadaalta.quizzengine.model.Category;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.loaders.DeckJsonLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameEngineTest {

    public static final String TEST_DECK_JSON = "test_deck.json";

    @Test
    void play() throws IOException {
        String gameName = "Game test";

        Deck deck = createQuestionsDeck("Deck - GameEngine Main test");
        List<Player> players  = createPlayers(deck);
        GameEngine gameEngine = new GameEngine(gameName, deck, players);

        GameEngineClient gameEngineClientMock = mock(GameEngineClient.class);
        gameEngine.register(gameEngineClientMock);
        gameEngine.play();

        verify(gameEngineClientMock, times(1)).notifyGameStarts();
        verify(gameEngineClientMock, atLeast(1)).notifyRoundStarts(1);
        verify(gameEngineClientMock, atLeast(1)).notifyTurnStarts(players.get(0));
        verify(gameEngineClientMock, atLeast(1)).notifyTurnStarts(players.get(1));
        verify(gameEngineClientMock, atLeast(1)).notifyTurnEnds(players.get(0));
        verify(gameEngineClientMock, atLeast(1)).notifyTurnEnds(players.get(1));
        verify(gameEngineClientMock, atLeast(1)).notifyRoundEnds(1);
        verify(gameEngineClientMock, times(1)).notifyGameStarts();

    }

    private List<Player> createPlayers(Deck deck) {

        List<Player> players = new ArrayList<>();

        Set<Category> categories = deck.getCategories();
        Player player1 = new PlayerTest("Player 1", categories);
        Player player2 = new PlayerTest("Player 2", categories);

        players.add(player1);
        players.add(player2);

        return players;
    }

    private Deck createQuestionsDeck(String name) throws IOException {
        TestUtils testUtils = new TestUtils();
        String deckFilename = testUtils.getFileFromResources(TEST_DECK_JSON).getAbsolutePath();

        DeckJsonLoader deckJsonLoader = new DeckJsonLoader();
        Deck deck = deckJsonLoader.loadFromFilename(name, deckFilename);

        return deck;
    }

    static class PlayerTest extends Player {

        public PlayerTest(String name, Collection<Category> categories) {
            super(name, categories);
        }

        @Override
        public int getChoice() {
            return 0;
        }

    }

}