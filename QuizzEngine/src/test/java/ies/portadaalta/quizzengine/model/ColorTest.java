package ies.portadaalta.quizzengine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void hexColorTest() {
        // Pure colors
        assertColor("#FF0000", 255, 0, 0); // RED
        assertColor("#00FF00", 0, 255, 0); // GREEN
        assertColor("#0000FF", 0, 0, 255); // BLUE
        // Random colors
        assertColor("#B038A4", 176, 56, 164); // PINK
        assertColor("#b038a4", 176, 56, 164); // PINK lowercase
    }

    private void assertColor(String hexColor, int red, int green, int blue) {
        Color colorBlue = new Color(hexColor);
        assertEquals(red, colorBlue.getRed());
        assertEquals(green, colorBlue.getGreen());
        assertEquals(blue, colorBlue.getBlue());
    }

}