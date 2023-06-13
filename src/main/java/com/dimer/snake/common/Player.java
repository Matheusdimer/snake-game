package com.dimer.snake.common;

import java.util.Arrays;

import static com.dimer.snake.common.Properties.GAME_SIZE;

public class Player {
    private final int[] x = new int[GAME_SIZE * GAME_SIZE];

    private final int[] y = new int[GAME_SIZE * GAME_SIZE];

    private int dots = 3;

    private Movement movement = Movement.RIGHT;

    private final String name;

    private final int number;

    public Player(String name, int number) {
        this.name = name;
        this.number = number;

        for (int dotIndex = 0; dotIndex < dots; dotIndex++) {
            x[dotIndex] = dotIndex;
            y[dotIndex] = 0;
        }
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public String getName() {
        return name;
    }

    public void move() {
        boolean first = true;
        int xAnterior = 0, yAnterior = 0;

        for (int dot = dots - 1; dot >= 0; dot--) {
            int xHead = x[dot];
            int yHead = y[dot];

            if (first) {
                xAnterior = xHead;
                yAnterior = yHead;

                switch (movement) {
                    case UP:
                        xHead--;
                        break;
                    case DOWN:
                        xHead++;
                        break;
                    case LEFT:
                        yHead--;
                        break;
                    case RIGHT:
                        yHead++;
                        break;
                }
                first = false;
            } else {
                int xAux = xHead, yAux = yHead;

                xHead = xAnterior;
                yHead = yAnterior;

                xAnterior = xAux;
                yAnterior = yAux;
            }

            x[dot] = xHead < GAME_SIZE ? (xHead >= 0 ? xHead : GAME_SIZE - 1) : 0;
            y[dot] = yHead < GAME_SIZE ? (yHead >= 0 ? yHead : GAME_SIZE - 1) : 0;
        }
    }

    public void renderToGround(int[][] ground) {
        for (int[] ints : ground) {
            Arrays.fill(ints, 0);
        }

        for (int dotIndex = 0; dotIndex < dots; dotIndex++) {
            int xPos = x[dotIndex], yPos = y[dotIndex];
            ground[xPos][yPos] = number;
        }
    }

    public Movement getMovement() {
        return movement;
    }
}
