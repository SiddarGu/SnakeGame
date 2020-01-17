package model;

import java.awt.*;

public enum Cell {
    EMPTY(Color.BLACK, "B"),
    SNAKE(new Color(22, 124, 128), "G"),
    FOOD(Color.YELLOW, "Y");

    private final Color color;
    private final String name;

    Cell(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }
}
