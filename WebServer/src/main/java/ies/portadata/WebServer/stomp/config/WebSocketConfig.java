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

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final Deck deck;

    public WebSocketConfig() throws IOException {
        String jsonDeckPath = "static/assets/decks/default.json";
        File deckFile = getFileFromResources(jsonDeckPath);

        DeckJsonLoader deckJsonLoader = new DeckJsonLoader();
        Deck deck = deckJsonLoader.loadFromFile("Deck de prueba", deckFile);;
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
    public Deck getDeck() throws IOException {
        return deck;
    }

    @Bean
    public GameEngine getGameEngine() throws IOException {
        Player player = new Player("Player1", deck.getCategories());
        GameEngine gameEngine = new GameEngine("Test Game", deck, List.of(player));
        return gameEngine;
    }


    private static File getFileFromResources(String filename) {
        //ClassLoader classLoader = getClass().getClassLoader();
        ClassLoader classLoader = WebServerApplication.class.getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        return file;
    }
}
