package com.dimer.snake.server;

import com.dimer.snake.common.Movement;
import com.dimer.snake.common.Player;
import com.dimer.snake.common.Properties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameController extends Thread {
    public static final GameController INSTANCE = new GameController();

    private final int[][] ground = new int[Properties.GAME_SIZE][Properties.GAME_SIZE];

    private final Map<String, Player> players = new ConcurrentHashMap<>();

    private int lastPlayerNumber = 10;

    private GameController() {
    }

    @Override
    public void run() {
        while (true) {
            for (Player player : players.values()) {
                player.move();
                player.renderToGround(ground);
            }

            try {
                sleep(Properties.DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized String newPlayer() {
        int number = lastPlayerNumber++;
        String name = "Player" + number;
        players.put(name, new Player(name, number));
        return name;
    }

    public synchronized Player getPlayer(String name) {
        return players.get(name);
    }

    public synchronized void movePlayer(String name, Movement movement) {
        getPlayer(name).setMovement(movement);
    }

    public synchronized int[][] getGround() {
        return ground;
    }

    public synchronized void removePlayer(String playerName) {
        players.remove(playerName);
    }
}
