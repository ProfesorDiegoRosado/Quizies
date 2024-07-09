package ies.portadaalta.engine.model;

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
}
