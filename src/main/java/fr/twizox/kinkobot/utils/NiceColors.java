package fr.twizox.kinkobot.utils;

import java.awt.*;

public enum NiceColors {
    DEFAULT(new Color(47, 49, 54)),
    GREEN(new Color(128, 255, 150)),
    RED(new Color(255, 128, 139)),
    BLUE(new Color(128, 217, 255)),
    ORANGE(new Color(255, 204, 128));
    private final Color color;

    NiceColors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Color getRandomColor() {
        return Color.getHSBColor((float) Math.random(), 0.5f, 1.0f);
    }

}
