package ies.portadata.WebServer;

import ies.portadaalta.gameengine.model.GameEngine;
import ies.portadaalta.gameengine.model.Player;
import ies.portadaalta.quizzengine.model.Deck;
import ies.portadaalta.quizzengine.model.loaders.DeckJsonLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class WebServerApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(WebServerApplication.class, args);

	}



}
