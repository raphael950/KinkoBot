package fr.twizox.kinkobot.utils;

import java.awt.*;

public class Colors {

    public static Color getRandomColor() {
        return Color.getHSBColor((float) Math.random(), 0.5f, 1.0f);
    }

}
