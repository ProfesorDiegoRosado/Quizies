package ies.portadaalta.quizzengine.model;

import java.util.Objects;

public class Category {

    private final String name;
    private final String Description;
    private final Color color;

    public Category(String name, String description, Color color) {
        this.name = name;
        Description = description;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return Description;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", Description='" + Description + '\'' +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name) && Objects.equals(Description, category.Description) && Objects.equals(color, category.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, Description, color);
    }
}
