package com.dimer.snake.client;

import com.dimer.snake.common.GroundPackage;
import com.dimer.snake.common.Movement;
import com.dimer.snake.common.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;

public class Board extends JPanel {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = B_WIDTH / Properties.GAME_SIZE;


    private int[][] ground = new int[Properties.GAME_SIZE][Properties.GAME_SIZE];
    private boolean inGame = true;

    private Image ball;
    private Image apple;
    private Image head;

    private final DataOutputStream out;
    private Movement actualMovement;

    public Board(DataOutputStream out) {
        this.out = out;
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/main/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/main/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/main/resources/head.png");
        head = iih.getImage();
    }

    public void render(GroundPackage groundPackage) {
        this.ground = groundPackage.getGround();
        this.actualMovement = groundPackage.getActualMovement();
        this.inGame = groundPackage.isInGame();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            doDrawing(g);
        } else {
            gameOver(g);
        }
    }

    private void doDrawing(Graphics g) {
        for (int y = 0; y < ground.length; y++) {
            for (int x = 0; x < ground[y].length; x++) {
                int dot = ground[y][x];

                if (dot == Properties.APPLE) {
                    g.drawImage(apple, x * DOT_SIZE, y * DOT_SIZE, this);
                } else if (dot == Properties.DEAD_PLAYER) {
                    g.drawImage(head, x * DOT_SIZE, y * DOT_SIZE, this);
                } else if (Math.abs(dot) >= 10) {
                    g.drawImage(dot < 0 ? head : ball, x * DOT_SIZE, y * DOT_SIZE, this);
                }
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            try {
                if ((key == KeyEvent.VK_LEFT) && (actualMovement != Movement.RIGHT)) {
                    out.writeInt(Movement.LEFT.ordinal());
                }

                if ((key == KeyEvent.VK_RIGHT) && (actualMovement != Movement.LEFT)) {
                    out.writeInt(Movement.RIGHT.ordinal());
                }

                if ((key == KeyEvent.VK_UP) && (actualMovement != Movement.DOWN)) {
                    out.writeInt(Movement.UP.ordinal());
                }

                if ((key == KeyEvent.VK_DOWN) && (actualMovement != Movement.UP)) {
                    out.writeInt(Movement.DOWN.ordinal());
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
