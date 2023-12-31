package com.dimer.snake.client;

import com.dimer.snake.common.GroundPackage;
import com.dimer.snake.common.Movement;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.dimer.snake.common.Properties.*;

public class Board extends JPanel {

    private final int B_WIDTH = GAME_SIZE * DOT_SIZE;
    private final int B_HEIGHT = GAME_SIZE * DOT_SIZE;


    private int[][] ground = new int[GAME_SIZE][GAME_SIZE];
    private boolean inGame = true;

    private Image apple;

    private DataOutputStream out;
    private Movement actualMovement;
    private int score;
    private GroundPackage groundPackage;

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
        try {
            ImageIcon iia = new ImageIcon(
                    IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("apple.png"))
            );
            apple = iia.getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(GroundPackage groundPackage) {
        this.groundPackage = groundPackage;
        this.ground = groundPackage.getGround();
        this.actualMovement = groundPackage.getActualMovement();
        this.inGame = groundPackage.isInGame();
        if (inGame) {
            this.score = groundPackage.getScore();
        }

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
        drawScore(g);

        for (int y = 0; y < ground.length; y++) {
            for (int x = 0; x < ground[y].length; x++) {
                int dot = ground[y][x];

                if (dot == APPLE) {
                    g.setColor(null);
                    g.drawImage(apple, x * DOT_SIZE, y * DOT_SIZE, DOT_SIZE, DOT_SIZE, this);
                } else if (dot == DEAD_PLAYER) {
                    g.setColor(DEAD_PLAYER_COLOR);
                    g.fillOval(x * DOT_SIZE, y * DOT_SIZE, DOT_SIZE, DOT_SIZE);
                } else if (Math.abs(dot) >= 10) {
                    g.setColor(dot < 0 ? HEAD_COLOR : groundPackage.getPlayerColor(dot));
                    g.fillOval(x * DOT_SIZE, y * DOT_SIZE, DOT_SIZE, DOT_SIZE);
                }
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {
        drawScore(g);
        String msg = "Game Over";
        String restartMsg = "Press enter to restart game";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        g.drawString(restartMsg, (B_WIDTH - metr.stringWidth(restartMsg)) / 2, B_HEIGHT / 2 + 20);
    }

    private void drawScore(Graphics g) {
        String msg = "Score: " + score;
        Font small = new Font("Helvetica", Font.BOLD, 14);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, 5, 20);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            try {
                if (key == KeyEvent.VK_ENTER && !inGame) {
                    SnakeClient client = SnakeClient.INSTANCE;
                    client.stopConnection();
                    new Thread(() -> client.start(null, SnakeClient.ip)).start();
                    return;
                }

                if ((key == KeyEvent.VK_LEFT) && (actualMovement != Movement.RIGHT)) {
                    out.writeInt(Movement.LEFT.ordinal());
                    return;
                }

                if ((key == KeyEvent.VK_RIGHT) && (actualMovement != Movement.LEFT)) {
                    out.writeInt(Movement.RIGHT.ordinal());
                    return;
                }

                if ((key == KeyEvent.VK_UP) && (actualMovement != Movement.DOWN)) {
                    out.writeInt(Movement.UP.ordinal());
                    return;
                }

                if ((key == KeyEvent.VK_DOWN) && (actualMovement != Movement.UP)) {
                    out.writeInt(Movement.DOWN.ordinal());
                    return;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }
}
