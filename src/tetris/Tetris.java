package tetris;

import javax.swing.*;
import java.awt.*;

/**
 * Main class <code>Tetris</code> extends <code>JFrame</code> and build window with <code>GridLayout</code>.
 * This window contains board panel (play panel) and right information panel.
 */
public class Tetris extends JFrame {
    /* width size frame */
    final static int WIDTH_WINDOW = 600;

    /* height size frame */
    final static int HEIGHT_WINDOW = 500;

    public Board board;
    public RightPanel rightPanel;

    /**
     * set layout, add board panel and right information panel
     */
    public Tetris() {
        setLayout(new GridLayout(1, 2, 0, 0));
        board = new Board(this);
        rightPanel = new RightPanel(this);
        add(board);
        add(rightPanel);
        board.start();

        setTitle("Tetris");
        setSize(WIDTH_WINDOW, HEIGHT_WINDOW);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Tetris game = new Tetris();
        game.setLocationRelativeTo(null);
        game.setResizable(false);
        game.setVisible(true);
    }
}
