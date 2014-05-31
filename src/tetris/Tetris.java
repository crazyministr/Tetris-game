package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tetris extends JFrame {
    JLabel statusBar;

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
        createMenuBar();
        statusBar = new JLabel(" 0");
        Board board = new Board(this);
        add(board, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(statusBar, BorderLayout.NORTH);

        add(leftPanel, BorderLayout.EAST);
        board.start();
        setSize(210, 400);
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
