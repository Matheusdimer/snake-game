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
            if (window != null) {
                window.dispose();
            }
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
        int port = Properties.SERVER_PORT;

        if (ip.contains(":")) {
            String[] split = ip.split(":");
            ip = split[0];
            port = Integer.parseInt(split[1]);
        }

        System.out.println("[CLIENT] Iniciando conexão com o servidor.");
        clientSocket = new Socket(ip, port, null, Properties.randomInt(15000, 25000));

        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();

            clientSocket = new Socket(ip, Properties.SERVER_PORT, null, Properties.randomInt(15000, 25000));
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        }
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
            System.out.println("[CLIENT] Encerrando conexão com o servidor.");
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException exception) {
            System.out.println("[CLIENT] Failed to close connection with server");
            exception.printStackTrace();
        }
    }
}
