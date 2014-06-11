package tetris;

import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {
    final static int WIDTH_WINDOW = 400;
    final static int HEIGHT_WINDOW = 400;
    public Board board;
    public RightPanel rightPanel;

    private String msg = "p(P) - pause\nSpace - shape drop down\n";

    public Tetris() {
        JOptionPane.showMessageDialog(null, msg, "HOW TO PLAY", JOptionPane.INFORMATION_MESSAGE);
        setLayout(new GridLayout(1, 2, 0, 0));
        board = new Board(this);
        rightPanel = new RightPanel(this);
        add(board);
        add(rightPanel);
        board.start();

        setSize(WIDTH_WINDOW, HEIGHT_WINDOW);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Tetris game = new Tetris();
        game.setLocationRelativeTo(null);
        game.setResizable(false);
        game.setVisible(true);
    }
}
