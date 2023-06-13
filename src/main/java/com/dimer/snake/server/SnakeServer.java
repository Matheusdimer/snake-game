package com.dimer.snake.server;

import com.dimer.snake.common.Properties;

import java.io.IOException;
import java.net.ServerSocket;

public class SnakeServer {

    public static void main(String[] args) {
        SnakeServer server = new SnakeServer();
        server.start(Properties.SERVER_PORT);
    }

    public void start(int port) {
        GameController.INSTANCE.start();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                new SnakeClientReceiverThread(serverSocket.accept()).start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
