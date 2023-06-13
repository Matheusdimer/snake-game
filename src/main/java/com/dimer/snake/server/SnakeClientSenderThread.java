package com.dimer.snake.server;

import com.dimer.snake.common.GroundPackage;
import com.dimer.snake.common.Player;
import com.dimer.snake.common.Properties;

import java.io.*;
import java.net.Socket;

public class SnakeClientSenderThread extends Thread {

    private final Socket clientSocket;
    private final GameController gameController;
    private final String playerName;

    public SnakeClientSenderThread(Socket clientSocket, String playerName) {
        this.clientSocket = clientSocket;
        this.gameController = GameController.INSTANCE;
        this.playerName = playerName;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

            while (clientSocket.isConnected()) {
                Player player = gameController.getPlayer(playerName);

                if (player == null) {
                    System.out.println("[SENDER] " + playerName + " desconectado.");
                    break;
                }

                GroundPackage groundPackage = new GroundPackage(
                        gameController.getGround(),
                        player.getMovement()
                );

                out.writeUnshared(groundPackage);

                sleep(Properties.DELAY / 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
