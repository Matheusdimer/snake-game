package com.dimer.snake.server;

import com.dimer.snake.common.GroundPackage;
import com.dimer.snake.common.Movement;
import com.dimer.snake.common.Properties;

import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SnakeClientReceiverThread extends Thread {

    private final Socket clientSocket;
    private final GameController gameController;

    public SnakeClientReceiverThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.gameController = GameController.INSTANCE;
    }

    @Override
    public void run() {
        String playerName = null;

        try {
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            playerName = gameController.newPlayer();
            System.out.println("[RECEIVER] " + playerName + " conectado.");

            new SnakeClientSenderThread(clientSocket, playerName).start();

            while (clientSocket.isConnected()) {
                int command = in.readInt();
                Movement movement = Movement.fromValue(command);

                if (movement != null) {
                    System.out.println("[RECEIVER] " + playerName + " se moveu: " + movement);
                    gameController.movePlayer(playerName, movement);
                }
            }
        } catch (Exception e) {
            gameController.removePlayer(playerName);
            System.out.println("[RECEIVER] " + playerName + " sendo removido.");
        }
    }
}
