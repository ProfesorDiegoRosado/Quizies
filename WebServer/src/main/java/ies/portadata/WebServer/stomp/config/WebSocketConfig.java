package ies.portadata.WebServer.stomp.config;

import ies.portadaalta.gameengine.model.GameEngine;
import ies.portadaalta.gameengine.model.Player;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.loaders.DeckJsonLoader;
import ies.portadata.WebServer.WebServerApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.io.*;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final Deck deck;

    public WebSocketConfig() throws IOException {
        String jsonDeckPath = "static/assets/decks/";
        String jsonFilename = System.getenv("JSON_DECK_FILENAME");
        String fullPathName = jsonDeckPath + jsonFilename;
        BufferedReader deckBufferedReader = getFileFromResources(fullPathName);

        DeckJsonLoader deckJsonLoader = new DeckJsonLoader();
        String fileContent = String.join("\n",deckBufferedReader.lines().toList());
        Deck deck = deckJsonLoader.loadFromString("Deck de prueba", fileContent);
        this.deck = deck;

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/quizies");
    }

    @Bean
    public Deck getDeck() {
        return deck;
    }

    @Bean
    public GameEngine getGameEngine() {
        Player player = new Player("Player1", deck.getCategories());
        GameEngine gameEngine = new GameEngine("Test Game", deck, List.of(player));
        return gameEngine;
    }


    private static BufferedReader getFileFromResources(String filename) {
        ClassLoader classLoader = WebServerApplication.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return bufferedReader;
    }

}
