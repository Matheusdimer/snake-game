package com.dimer.snake.common;

import java.io.Serializable;

public class GroundPackage implements Serializable {
    private final int[][] ground = new int[Properties.GAME_SIZE][Properties.GAME_SIZE];

    private final Movement actualMovement;

    private final boolean inGame;

    public GroundPackage(int[][] ground, Movement actualMovement, boolean inGame) {
        this.actualMovement = actualMovement;
        this.inGame = inGame;
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
}
