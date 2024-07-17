package ies.portadaalta.quizzengine.model;

import java.util.Set;

public class Engine {

    private final Deck deck;

    public Engine(Deck deck) {
        this.deck = deck;
    }

    public Set<Category> getCategories() {
        return deck.getCategories();
    }

    public Question getNextQuestion(Category category) {
        return deck.getNextQuestion(category);
    }
}
