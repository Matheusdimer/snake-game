package com.dimer.snake.server;

import com.dimer.snake.common.Movement;
import com.dimer.snake.common.Player;
import com.dimer.snake.common.Properties;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dimer.snake.common.Properties.GAME_SIZE;

public class GameController extends Thread {
    public static final GameController INSTANCE = new GameController();

    private final int[][] ground = new int[GAME_SIZE][GAME_SIZE];

    private final Map<String, Player> players = new ConcurrentHashMap<>();

    private int lastPlayerNumber = 10;

    private int xApple, yApple;
    private boolean hasApple = false;

    private GameController() {
    }

    @Override
    public void run() {
        while (true) {
            if (!hasApple) {
                hasApple = true;
                while (true) {
                    xApple = (int) (Math.random() * (GAME_SIZE - 1));
                    yApple = (int) (Math.random() * (GAME_SIZE - 1));

                    if (ground[xApple][yApple] == 0) {
                        break;
                    }
                }
            }

            for (int[] ints : ground) {
                Arrays.fill(ints, 0);
            }

            for (Player player : players.values()) {
                player.move();
                player.renderToGround(ground);

                if (player.isDead()) {
                    removePlayer(player.getName());
                    continue;
                } else if (player.hasHeadClash()) {
                    Player playerCashed = findPlayerByNumber(player.getOtherPlayerClashed());
                    if (playerCashed != null) {
                        decideWhoKill(player, playerCashed);
                    }
                    continue;
                }

                if (hasApple) {
                    boolean ate = player.checkApple(xApple, yApple);
                    hasApple = !ate;
                }
            }

            if (hasApple) {
                ground[xApple][yApple] = Properties.APPLE;
            }

            try {
                sleep(Properties.DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void decideWhoKill(Player player1, Player player2) {
        // Se os dois players tem o mesmo tamanho, é tirado a sorte para ver quem morre :)
        if (player1.length() == player2.length()) {
            if (Math.random() <= 0.5) {
                player1.kill();
            } else {
                player2.kill();
            }
        // Se não, morre quem for menor
        } else if (player1.length() > player2.length()) {
            player2.kill();
        } else {
            player1.kill();
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

    private Player findPlayerByNumber(int number) {
        for (Player player : players.values()) {
            if (player.getNumber() == number) {
                return player;
            }
        }

        return null;
    }
}
