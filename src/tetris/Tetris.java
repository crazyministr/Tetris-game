package tetris;

import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {
    final static int WIDTH_WINDOW = 400;
    final static int HEIGHT_WINDOW = 400;
    public Board board;
    public RightPanel rightPanel;

    private String msg = "hfksjdhfksf sfh ksdjfhk jsdfh jsdhfkjsdh fsdh fksd fshdf ksdhjfh sdf";

    public Tetris() {
        JOptionPane.showConfirmDialog(null, msg, "HOW TO PLAY", JOptionPane.YES_OPTION);
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
