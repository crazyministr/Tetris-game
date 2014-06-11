package tetris;

import javax.accessibility.AccessibleComponent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RightPanel extends JPanel implements ActionListener {
    private JLabel statusBar = new JLabel("points: 0");

    public RightPanel(final Tetris parent) {
        setSize(200, 400);
        setLayout(new BorderLayout());

        JButton testButton = new JButton("TEST");
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.board.requestFocus(true);
            }
        });

        add(testButton, BorderLayout.NORTH);
        add(statusBar);
    }

    public JLabel getStatusBar() {
        return statusBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("clicked TEST");
        this.requestFocus(false);
    }
}
