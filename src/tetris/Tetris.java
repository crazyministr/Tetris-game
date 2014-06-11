package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tetris extends JFrame {
    JLabel statusBar;
    final static int WIDTH_WINDOW = 400;
    final static int HEIGHT_WINDOW = 400;
    public Board board;
    public RightPanel rightPanel;

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuNewGame = new JMenu("New game");
        JMenu menuExit = new JMenu("Exit");
        JLabel statusMenu = new JLabel("Run");
        menuBar.add(menuNewGame);
        menuBar.add(menuExit);
        menuBar.add(statusMenu, BorderLayout.EAST);
        add(menuBar, BorderLayout.NORTH);
    }

    public Tetris() {
//        createMenuBar();
//        statusBar = new JLabel(" 0");
        setLayout(new GridLayout(1, 2, 0, 0));
//        add(statusBar);

        rightPanel = new RightPanel(this);
        board = new Board(this);
        add(board);
        add(rightPanel);

        board.start();
        setSize(WIDTH_WINDOW, HEIGHT_WINDOW);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public JLabel getStatusBar() {
        return statusBar;
    }

    public static void main(String[] args) {
        Tetris game = new Tetris();
        game.setLocationRelativeTo(null);
        game.setResizable(false);
        game.setVisible(true);
    }
}
