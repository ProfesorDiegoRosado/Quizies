package ies.portadaalta.engine.exception;

import ies.portadaalta.engine.model.Category;

public class NoCategoryFoundException extends RuntimeException {

    private Category category;

    public NoCategoryFoundException(Category category) {
        super("The category " + category + " was not found in the Deck Engine.");
        this.category = category;
    }

}
