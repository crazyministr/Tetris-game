package tetris;

import javax.accessibility.AccessibleComponent;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class RightPanel extends JPanel {
    public String statusString = "status: ";
    public String pointsString = "points: ";
    public ShapePanel shapePanel;

    private JLabel statusBar = new JLabel(statusString + "None");
    private JLabel points = new JLabel("0");
    private final Tetris parent;

    public RightPanel(final Tetris parent) {
        this.parent = parent;
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        add(createStatusPanel(), BorderLayout.BEFORE_FIRST_LINE);
//        add(new JSeparator());
        add(createNextShapePanel(), BorderLayout.CENTER);
//        add(new JSeparator());
        add(createButtonsPanel(), BorderLayout.SOUTH);
    }

    private Component createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        buttonsPanel.setOpaque(false);

        JButton restartButton = new JButton("restart");
        JButton exitButton = new JButton("exit");
        JButton showRecordsButton = new JButton("records");
        JButton aboutButton = new JButton("about");

        buttonsPanel.add(new JLabel(pointsString));
        buttonsPanel.add(points);
        buttonsPanel.add(restartButton);
        buttonsPanel.add(exitButton);
        buttonsPanel.add(showRecordsButton);
        buttonsPanel.add(aboutButton);

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Timer timer = parent.board.getTimer();
                if (timer.isRunning()) {
//                    parent.board.pause();
                    int res = JOptionPane.showConfirmDialog(null,
                          "Are you sure to restart ?",
                          "RESTART",
                          JOptionPane.YES_NO_OPTION,
                          JOptionPane.QUESTION_MESSAGE);
                    if (res == JOptionPane.NO_OPTION) {
                        parent.board.requestFocus(true);
//                        parent.board.pause();
                        return;
                    }
//                    parent.board.pause();
                }
                parent.board.requestFocus(true);
                parent.board.initBoard();
                timer.start();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                parent.board.pause();
                int res = JOptionPane.showConfirmDialog(null,
                        "Are you sure to exit ?\nProgress will not be saving!",
                        "EXIT",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (res == JOptionPane.YES_OPTION) {
                    parent.board.getTimer().stop();
                    parent.dispose();
                } else {
//                    parent.board.pause();
                    parent.board.requestFocus(true);
                }
            }
        });

        showRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: show new frame with records from file record.txt
                parent.board.requestFocus(true);
            }
        });

        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: create new frame with info about author
                parent.board.requestFocus(true);
            }
        });

        return buttonsPanel;
    }

    private Component createNextShapePanel() {
        shapePanel = new ShapePanel();
        shapePanel.setShape(parent.board.nextShape);
        return shapePanel;
    }

    private Component createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);
        statusPanel.add(statusBar);
        return statusPanel;
    }

    public JLabel getStatusBar() {
        return statusBar;
    }

    public JLabel getPoints() {
        return points;
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
