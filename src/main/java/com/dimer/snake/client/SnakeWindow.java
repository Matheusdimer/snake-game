package com.dimer.snake.client;

import com.dimer.snake.common.GroundPackage;

import javax.swing.JFrame;
import java.io.DataOutputStream;

public class SnakeWindow extends JFrame {

    private final Board board;

    public SnakeWindow(DataOutputStream out) {
        board = new Board(out);
        add(board);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
    
    public void render(GroundPackage groundPackage) {
        board.render(groundPackage);
    }

    public void setOutput(DataOutputStream out) {
        board.setOut(out);
    }
}
