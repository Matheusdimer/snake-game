package com.dimer.snake.client;

import javax.swing.*;
import java.util.function.BiConsumer;

public class ConfigWindow extends JFrame {
    private JPanel panel;
    private JTextField ipField;
    private JButton startButton;
    private JLabel label;

    public ConfigWindow(BiConsumer<ConfigWindow, String> onStart) {
        configureButtonListener(onStart);

        ipField.setText("localhost");
        setTitle("Configuration");
        setContentPane(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setResizable(false);
        setVisible(true);
    }

    private void configureButtonListener(BiConsumer<ConfigWindow, String> onStart) {
        startButton.addActionListener(e -> {
            String ip = ipField.getText();

            if (ip == null || ip.isEmpty()) {
                return;
            }

            new Thread(() -> onStart.accept(this, ip)).start();
        });
    }

    private void createUIComponents() {
        startButton = new JButton("Start");
    }
}
