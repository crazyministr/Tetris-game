package tetris;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class RightPanel extends JPanel {
    public ShapePanel shapePanel;

    private JLabel bestResult;
    private JLabel statusBar = new JLabel("None");
    private JLabel points = new JLabel("0");
    private JLabel level = new JLabel("1");
    private final Tetris parent;

    public RightPanel(final Tetris parent) {
        this.parent = parent;
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 3));
        setLayout(new BorderLayout());
        add(createStatusPanel(), BorderLayout.NORTH);
        add(createNextShapePanel(), BorderLayout.CENTER);
        add(createButtonsPanel(), BorderLayout.SOUTH);
    }

    private Component aboutAuthorLink() {
        JLabel aboutAuthorLabel = new JLabel("<html><font style=\"color: blue; text-decoration: underline;\">about author</font></html>");

        aboutAuthorLabel.setHorizontalAlignment(JLabel.RIGHT);
        aboutAuthorLabel.setVerticalAlignment(JLabel.BOTTOM);
        aboutAuthorLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aboutAuthorLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JOptionPane.showMessageDialog(null,
                        "Malashenkov Anton",
                        "ABOUT AUTHOR",
                        JOptionPane.INFORMATION_MESSAGE);
                parent.board.requestFocus(true);
            }
        });
        return aboutAuthorLabel;
    }

    private Component howToPlayLink() {
        JLabel howToPlayLabel = new JLabel("<html><font style=\"color: blue; text-decoration: underline;\">how to play</font></html>");
        howToPlayLabel.setHorizontalAlignment(JLabel.RIGHT);
        howToPlayLabel.setVerticalAlignment(JLabel.BOTTOM);
        howToPlayLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        howToPlayLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JOptionPane.showMessageDialog(null,
                        "nothing",
                        "HOW TO PLAY",
                        JOptionPane.INFORMATION_MESSAGE);
                parent.board.requestFocus(true);
            }
        });
        return howToPlayLabel;
    }

    private Component createButtonsPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setOpaque(false);

        JPanel buttonsPanel = new JPanel();
        mainPanel.add(buttonsPanel);
        mainPanel.add(howToPlayLink());
        mainPanel.add(aboutAuthorLink());

        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new GridLayout(5, 2, 10, 3));

        JButton restartButton = new JButton("new game");
        JButton exitButton = new JButton("exit");

        bestResult = getBestResult();

        JLabel bestResultLabel = new JLabel("best score: ");
        JLabel pointsLabel = new JLabel("current points: ");
        JLabel levelLabel = new JLabel("level: ");

        bestResultLabel.setHorizontalAlignment(JLabel.RIGHT);
        pointsLabel.setHorizontalAlignment(JLabel.RIGHT);
        levelLabel.setHorizontalAlignment(JLabel.RIGHT);

        buttonsPanel.add(levelLabel);
        buttonsPanel.add(level);
        buttonsPanel.add(pointsLabel);
        buttonsPanel.add(points);
        buttonsPanel.add(bestResultLabel);
        buttonsPanel.add(bestResult);
        buttonsPanel.add(restartButton);
        buttonsPanel.add(exitButton);

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (statusBar.getText().equals("game over")) {
                    parent.board.restart();
                    parent.board.requestFocus(true);
                    return;
                }
                boolean wasPaused = true;
                if (parent.board.getTimer().isRunning()) {
                    parent.board.pause();
                    wasPaused = false;
                }
                int res = JOptionPane.showConfirmDialog(null,
                      "Are you sure to start new game ?",
                      "NEW GAME",
                      JOptionPane.YES_NO_OPTION,
                      JOptionPane.QUESTION_MESSAGE);
                if (res == JOptionPane.NO_OPTION) {
                    parent.board.requestFocus(true);
                    if (!wasPaused) {
                        parent.board.pause();
                    }
                } else {
                    parent.board.restart();
                    parent.board.requestFocus(true);
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean wasPaused = true;
                if (parent.board.getTimer().isRunning()) {
                    parent.board.pause();
                    wasPaused = false;
                }
                int res = JOptionPane.showConfirmDialog(null,
                        "Are you sure to exit ?",
                        "EXIT",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (res == JOptionPane.YES_OPTION) {
                    parent.board.getTimer().stop();
                    parent.dispose();
                } else {
                    if (!wasPaused) {
                        parent.board.pause();
                    }
                    parent.board.requestFocus(true);
                }
            }
        });

        return mainPanel;
    }

    public JLabel getBestResult() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream("best_result.txt"), "UTF-8"));
        } catch (IOException e) {
            System.out.println("[ERROR] error get properties");
        }
        int res;
        String name;
        try {
            name = properties.getProperty("name", "Unknown");
            res = Integer.parseInt(properties.getProperty("points", "Unknown"));
        } catch (Exception ignored) {
            return new JLabel("Unknown");
        }
        return new JLabel(String.valueOf(res) + " (" + name + ")");
    }

    public void updateBestResult() {
        this.bestResult.setText(getBestResult().getText());
        System.out.println(this.bestResult.getText());
    }

    private Component createNextShapePanel() {
        shapePanel = new ShapePanel();
        shapePanel.setShape(parent.board.nextShape);

        JPanel mainPanel = new JPanel(new GridLayout(3, 3, 0, 0));
        mainPanel.setOpaque(false);
        mainPanel.add(new JLabel(""));
        mainPanel.add(new JLabel(""));
        mainPanel.add(new JLabel(""));
        mainPanel.add(new JLabel(""));
        mainPanel.add(shapePanel);
        mainPanel.add(new JLabel(""));
        mainPanel.add(new JLabel(""));
        mainPanel.add(new JLabel(""));
        mainPanel.add(new JLabel(""));

        return mainPanel;
    }

    private Component createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);
        statusPanel.add(new JLabel("status: "));
        statusPanel.add(statusBar);
        return statusPanel;
    }

    public JLabel getStatusBar() {
        return statusBar;
    }

    public JLabel getPoints() {
        return points;
    }

    public JLabel getLevel() {
        return level;
    }

    public void paintComponent(Graphics g) {
        try {
            Image backgroundImage = ImageIO.read(new File("images/right_panel_background_image.jpg"));
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
