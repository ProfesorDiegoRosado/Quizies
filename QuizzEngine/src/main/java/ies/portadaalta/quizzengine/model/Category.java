package ies.portadaalta.quizzengine.model;


import static ies.portadaalta.quizzengine.model.Color.*;
import java.util.Objects;
import java.util.List;



public class Category {

    private static final List<Color> DEFAULT_COLORS = List.of(
            BLUE, PURPLE, YELLOW, PINK, GREEN, ORANGE
    );

    private final String name;
    private final String description;
    private final Color color;

    public Category(String name, String description, Color color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public Category(String name, String description, String hexColor) {
        this.name = name;
        this.description = description;
        this.color = new Color(hexColor);
    }

    public Category(String name, String description, int defaultColorIndex) {
        this.name = name;
        this.description = description;
        this.color = getDefaultColor(defaultColorIndex);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", Description='" + description + '\'' +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name) && Objects.equals(description, category.description) && Objects.equals(color, category.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, color);
    }

    public static List<Color> getDefaultColors() {
        return DEFAULT_COLORS;
    }

    public static Color getDefaultColor(int i) {
        return DEFAULT_COLORS.get(i);
    }


}
