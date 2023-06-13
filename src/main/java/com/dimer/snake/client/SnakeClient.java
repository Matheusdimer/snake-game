package com.dimer.snake.client;

import com.dimer.snake.common.GroundPackage;
import com.dimer.snake.common.Properties;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class SnakeClient {
    private Socket clientSocket;
    private ObjectInputStream in;
    private DataOutputStream out;

    public static void main(String[] args) {
        SnakeClient client = new SnakeClient();
        client.startConnection("localhost", Properties.SERVER_PORT);

        try {
            client.render();
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.stopConnection();
    }

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException exception) {
            throw new RuntimeException("Failed to acquire connection with server", exception);
        }
    }

    public void render() throws IOException, ClassNotFoundException {
        SnakeWindow gameWindow = new SnakeWindow(out);

        while (clientSocket.isConnected()) {
            GroundPackage groundPackage = (GroundPackage) in.readUnshared();
            gameWindow.render(groundPackage);
        }
    }

    public void stopConnection() {
        try {
            in.close();
            clientSocket.close();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to close connection with server", exception);
        }
    }

    public void clearConsole() {
        for(int i = 0; i < 80*300; i++) // Default Height of cmd is 300 and Default width is 80
            System.out.print("\b");
    }
}
