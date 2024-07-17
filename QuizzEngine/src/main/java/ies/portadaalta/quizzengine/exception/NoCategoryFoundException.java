package ies.portadaalta.quizzengine.exception;

import ies.portadaalta.quizzengine.model.Category;

public class NoCategoryFoundException extends RuntimeException {

    private final Category category;

    public NoCategoryFoundException(Category category) {
        super("The category " + category + " was not found in the Deck Engine.");
        this.category = category;
    }

}
