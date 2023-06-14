package com.dimer.snake.common;

public class Properties {
    public static final int SERVER_PORT = 9090;
    public static final int CLIENT_PORT = 9091;
    public static final int DELAY = 100;
    public static final int GAME_SIZE = 30;
    public static final int APPLE = 1;
    public static final int DEAD_PLAYER = 2;

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * max) + min;
    }

}
