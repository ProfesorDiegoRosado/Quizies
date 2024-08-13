package ies.portadaalta.quizzengine.model;

public class Color {

    // Default categories colors
    public static final Color BLUE = new Color("#429CE2");
    public static final Color PURPLE = new Color("#9D63D3");
    public static final Color YELLOW = new Color("#F2C537");
    public static final Color PINK = new Color("#FF3F8B");
    public static final Color GREEN = new Color("#8EC364");
    public static final Color ORANGE = new Color("#FFAF45");

    private final int red;
    private final int green;
    private final int blue;

    public Color(int red, int green, int blue) {
        assert red <=255 && red >=0;
        assert green <=255 && red >=0;
        assert blue <=255 && red >=0;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Color constructor from RGB hexadecimal string.
     * The String MUST start with # character. The size of the string MUST be 7 (# and 6 for hex RGB colors)
     * @param hexColorString String of color in hex RGB format (e.g.: #FFFFFF)
     */
    public Color(String hexColorString) {
        assert hexColorString.startsWith("#");
        assert hexColorString.length()==7;
        this.red = Integer.valueOf(hexColorString.substring(1, 3), 16);
        this.green = Integer.valueOf(hexColorString.substring(3, 5), 16);
        this.blue = Integer.valueOf(hexColorString.substring(5, 7), 16);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public String getHexString() {
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    @Override
    public String toString() {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                "," + getHexString() +
                '}';
    }
}
