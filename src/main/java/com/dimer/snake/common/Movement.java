package com.dimer.snake.common;

public enum Movement {
    UP, DOWN, LEFT, RIGHT;

    public static Movement fromValue(int value) {
        for (Movement movement : values()) {
            if (movement.ordinal() == value) {
                return movement;
            }
        }
        return null;
    }
}
