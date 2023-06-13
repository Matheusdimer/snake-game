package com.dimer.snake.client;

import com.dimer.snake.common.GroundPackage;
import com.dimer.snake.common.Properties;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class SnakeClient {
    public static final SnakeClient INSTANCE = new SnakeClient();

    public static String ip;

    private Socket clientSocket;
    private ObjectInputStream in;
    private DataOutputStream out;
    private SnakeWindow gameWindow;

    public static void main(String[] args) {
        new ConfigWindow(INSTANCE::start);
    }

    public void start(ConfigWindow window, String ip) {
        try {
            SnakeClient.ip = ip;
            startConnection(ip);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(window, "Erro ao conectar ao servidor:\n" + e.getMessage());
            window.dispose();
            return;
        }

        if (window != null) {
            window.dispose();
        }

        try {
            render();
        } catch (Exception e) {
            e.printStackTrace();
        }

        stopConnection();
    }

    public void startConnection(String ip) throws IOException {
        clientSocket = new Socket(ip, Properties.SERVER_PORT, null, Properties.CLIENT_PORT);
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void render() throws IOException, ClassNotFoundException {
        if (gameWindow == null) {
            gameWindow = new SnakeWindow(out);
        } else {
            gameWindow.setOutput(out);
        }

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
