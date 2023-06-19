package com.dimer.snake.common;

import java.awt.*;
import java.io.Serializable;
import java.util.Map;

public class GroundPackage implements Serializable {
    private final int[][] ground = new int[Properties.GAME_SIZE][Properties.GAME_SIZE];

    private final Movement actualMovement;

    private final boolean inGame;

    private final int score;

    private final Map<Integer, Color> playersColors;

    public GroundPackage(int[][] ground, Movement actualMovement, boolean inGame, int score, final Map<Integer, Color> playersColors) {
        this.actualMovement = actualMovement;
        this.inGame = inGame;
        this.score = score;
        this.playersColors = playersColors;
        for (int i = 0; i < ground.length; i++) {
            System.arraycopy(ground[i], 0, this.ground[i], 0, ground[i].length);
        }
    }

    public int[][] getGround() {
        return ground;
    }

    public Movement getActualMovement() {
        return actualMovement;
    }

    public boolean isInGame() {
        return inGame;
    }

    public int getScore() {
        return score;
    }

    public Color getPlayerColor(int number) {
        return playersColors.get(number);
    }
}
