package fr.twizox.kinkobot.utils;

import java.awt.*;

public class Colors {

    public static final Color DEFAULT = new Color(47, 49, 54);

    public static final Color NICE_GREEN = new Color(128, 255, 150);
    public static final Color NICE_RED = new Color(255, 128, 139);
    public static final Color NICE_BLUE = new Color(128, 217, 255);
    public static final Color NICE_ORANGE = new Color(255, 204, 128);

    public static Color getRandomColor() {
        return Color.getHSBColor((float) Math.random(), 0.5f, 1.0f);
    }

}
