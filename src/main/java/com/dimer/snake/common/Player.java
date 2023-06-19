package com.dimer.snake.common;

import com.dimer.snake.server.GameController;

import java.awt.*;

import static com.dimer.snake.common.Properties.GAME_SIZE;
import static com.dimer.snake.common.Properties.randomInt;

public class Player {
    private final int[] x = new int[GAME_SIZE * GAME_SIZE];

    private final int[] y = new int[GAME_SIZE * GAME_SIZE];

    private final GameController controller = GameController.INSTANCE;

    private final Color color = new Color(randomInt(0, 255), randomInt(0, 255), randomInt(0, 255));

    private int dots = 3;

    private Movement movement = Movement.RIGHT;

    private final String name;

    private final int number;

    private boolean dead;

    private boolean headClash;

    private int otherPlayerClashed;

    public Player(String name, int number, int initialX, int initialY) {
        if (initialX < 3) {
            throw new IllegalArgumentException("Initial X must be greater then 3");
        }

        this.name = name;
        this.number = number;

        for (int dotIndex = 0; dotIndex < dots; dotIndex++) {
            x[dotIndex] = initialX - dotIndex;
            y[dotIndex] = initialY;
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

    public void render() {
        final int[][] ground = controller.getGround();

        for (int dotIndex = 0; dotIndex < dots; dotIndex++) {
            int xPos = x[dotIndex], yPos = y[dotIndex];

            if (dotIndex == dots - 1) {
                // Indica que a cabeça do player se chocou com o corpo de outro player
                if (ground[xPos][yPos] >= 10) {
                    this.kill();
                } else if (ground[xPos][yPos] <= -10) {
                    // Indica que a cabeça do player se chocou com a cabeça de outro player
                    this.headClash = true;
                    this.otherPlayerClashed = Math.abs(ground[xPos][yPos]);
                }
            } else {
                int point = Math.abs(ground[xPos][yPos]);
                // Indica que há outro player se chocando no corpo do player atual
                if (point >= 10) {
                    final Player annotherPlayer = controller.getPlayer(point);
                    if (annotherPlayer != null) {
                        annotherPlayer.kill();
                    }
                }
            }

            ground[xPos][yPos] = dotIndex == dots - 1 ? -number : number;
        }
    }

    public Movement getMovement() {
        return movement;
    }

    public boolean checkApple(int xApple, int yApple) {
        int xHead = x[dots - 1];
        int yHead = y[dots - 1];

        if (xHead == xApple && yHead == yApple) {
            addSnakeLength();
            return true;
        }
        return false;
    }

    public void checkDeadPlayer() {
        boolean[][] deadPlayers = controller.getDeadPlayersMap();

        int xHead = x[dots - 1];
        int yHead = y[dots - 1];

        if (deadPlayers[xHead][yHead]) {
            addSnakeLength();
            deadPlayers[xHead][yHead] = false;
        }
    }

    private void addSnakeLength() {
        dots++;

        for (int dot = dots - 1; dot > 0; dot--) {
            x[dot] = x[dot - 1];
            y[dot] = y[dot - 1];
        }
    }

    public Player kill() {
        System.out.println("[PLAYER] " + name + " morreu.");
        dead = true;

        controller.removePlayer(getName());
        moveToDeadPlayers();

        return this;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean hasHeadClash() {
        return headClash;
    }

    public int getOtherPlayerClashed() {
        return otherPlayerClashed;
    }

    public int length() {
        return dots;
    }

    public int getNumber() {
        return number;
    }

    public Color getColor() {
        return color;
    }

    public void moveToDeadPlayers() {
        boolean[][] deadPlayersMap = controller.getDeadPlayersMap();

        for (int i = 0; i < dots; i++) {
            int xDot = x[i];
            int yDot = y[i];

            deadPlayersMap[xDot][yDot] = true;
        }
    }
}
