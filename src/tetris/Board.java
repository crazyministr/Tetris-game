package tetris;

import tetris.Shape.Tetrominoes;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Properties;

/**
 * Class <code>Board</code> draw all field with shapes and repaint him every time
 */
public class Board extends JPanel implements ActionListener {
    /* timeout on first level */
    private static final int START_TIMEOUT = 700;

    /* every level timeout decreases by <code>INDENT_TIMEOUT</code> */
    private static final int INDENT_TIMEOUT = 100;

    /* max length for winner name */
    private static final int MAX_LENGTH_NAME = 13;

    /* count point for jump to next level */
    private static final int POINTS_FOR_NEXT_LEVEL = 13;

    /* count cells of width */
    public static final int BOARD_WIDTH = 15;

    /* count cells of height */
    public static final int BOARD_HEIGHT = 25;

    public Shape currentShape;
    public Shape nextShape;

    private Timer timer;
    private JLabel level;
    private JLabel points;
    private int currentX = 0;
    private int currentY = 0;
    private JLabel statusBar;
    private final Tetris parent;
    private Tetrominoes[] board;
    private int countPoints = 0;
    private boolean isPaused = false;
    private boolean isStarted = false;
    private boolean isFallingFinished = false;

    private int currentTimeout = START_TIMEOUT;

    /**
     * initialize shapes and board
     * @param parent class parent
     */
    public Board(Tetris parent) {
        this.parent = parent;
        setFocusable(true);
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        addKeyListener(new TAdapter());

        currentShape = new Shape();
        currentShape.setRandomShape();
        currentX = BOARD_WIDTH / 2 + 1;
        currentY = BOARD_HEIGHT - 1 + currentShape.minY();
        nextShape = new Shape();
        nextShape.setRandomShape();

        board = new Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
    }

    /**
     * processing event every <code>currentTimeout milliseconds</code>
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newShapes();
        } else {
            oneLineDown();
        }
    }

    /**
     *
     * @return cells width
     */
    int cellWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    /**
     *
     * @return cells height
     */
    int cellHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    /**
     * return shape from <code>board[x][y]</code>
     * @param x <code>X</code> coordinates board[x][y]
     * @param y <code>Y</code> coordinates board[x][y]
     * @return shape
     */
    Tetrominoes shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    /**
     *
     * @return timer
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * initialize all for start and start timer
     */
    public void start() {
        isStarted = true;
        isPaused = false;
        isFallingFinished = false;
        countPoints = 0;

        level = parent.rightPanel.getLevel();
        points = parent.rightPanel.getPoints();
        statusBar = parent.rightPanel.getStatusBar();
        points.setText("0");
        statusBar.setText("started");

        initBoard();
        currentTimeout = START_TIMEOUT;
        timer = new Timer(currentTimeout, this);
        timer.start();
        tryMove(currentShape, currentX, currentY);
    }

    /**
     * initialize board (call <code>initBoard()</code>) and call <code>start()</code>
     */
    public void restart() {
        timer.stop();
        initBoard();
        newShapes();
        repaint();
        start();
    }

    /**
     * set timer in pause
     */
    public void pause() {
        if (!isStarted) {
            return;
        }
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            statusBar.setText("paused");
        } else {
            timer.start();
            statusBar.setText("started");
        }
        repaint();
    }

    /**
     * drop shape down
     */
    private void dropDown() {
        int newY = currentY;
        while (newY > 0) {
            if (!tryMove(currentShape, currentX, newY - 1)) {
                break;
            }
            --newY;
        }
        shapeDropped();
    }

    /**
     * drop shape on one line down
     */
    private void oneLineDown() {
        if (!tryMove(currentShape, currentX, currentY - 1)) {
            shapeDropped();
        }
    }

    /**
     * initialize board
     */
    public void initBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetrominoes.EmptyShape;
        }
    }

    /**
     * update <code>board[][]</code> after shape drop down
     */
    private void shapeDropped() {
        for (int i = 0; i < 4; i++) {
            int x = currentX + currentShape.getX(i);
            int y = currentY - currentShape.getY(i);
            board[y * BOARD_WIDTH + x] = currentShape.getShapeName();
        }
        removeFullLines();
        if (!isFallingFinished) {
            newShapes();
        }
    }

    /**
     * generate new shape after shape drop down
     */
    private void newShapes() {
        if (nextShape == null) {
            nextShape = new Shape();
            nextShape.setRandomShape();
        }
        currentShape = nextShape;
        currentX = BOARD_WIDTH / 2 + 1;
        currentY = BOARD_HEIGHT - 1 + currentShape.minY();
        nextShape = new Shape();
        nextShape.setRandomShape();
        parent.rightPanel.shapePanel.setShape(nextShape);
        if (isStarted && !tryMove(currentShape, currentX, currentY)) {
            currentShape.setShape(Tetrominoes.EmptyShape);
            timer.stop();
            isStarted = false;
            statusBar.setText("game over");
            saveNewResult();
        }
    }

    /**
     * calling when status game is game over
     * Save new result in <code>best_result.txt</code> if old result is worse
     */
    private void saveNewResult() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream("best_result.txt"), "UTF-8"));
        } catch (IOException e) {
            System.out.println("[ERROR] error get properties");
        }
        int oldPoints = Integer.parseInt(properties.getProperty("points"));
        if (oldPoints >= Integer.parseInt(points.getText())) {
            JOptionPane.showMessageDialog(null,
                    "Your score is not the best",
                    "GAME OVER",
                    JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon("images/game_over.jpg"));
            return;
        }

        JTextField tf = new JTextField(new CharLimitDocument(MAX_LENGTH_NAME), "", 0);
        Object[] msg = {
                "Your score is the best\nEnter your name to save", tf
        };
        int res = JOptionPane.showConfirmDialog(null,
                msg,
                "Congratulations!",
                JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.CANCEL_OPTION || tf.getText().equals("")) {
            return;
        }
        properties.setProperty("name", tf.getText());
        properties.setProperty("points", points.getText());
        try {
            properties.store(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("best_result.txt"), "UTF-8")), null);
        } catch (IOException e) {
            System.out.println("[ERROR] error save new result");
        }
        parent.rightPanel.updateBestResult();
    }

    /**
     * try make move to one line down
     * @param newShape potential new shape
     * @param newX potential new <code>X</code> coordinates of shape
     * @param newY potential new <code>Y</code> coordinates of shape
     * @return true if can move down else false
     */
    private boolean tryMove(Shape newShape, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newShape.getX(i);
            int y = newY - newShape.getY(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }
            if (shapeAt(x, y) != Tetrominoes.EmptyShape) {
                return false;
            }
        }
        currentShape = newShape;
        currentX = newX;
        currentY = newY;
        repaint();
        return true;
    }

    /**
     * Find full line and remove their.
     * If removes <code>X</code> lines then add <code>X * 2 - 1</code> point
     */
    private void removeFullLines() {
        int cntFullLines = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; --i) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (shapeAt(j, i) == Tetrominoes.EmptyShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                ++cntFullLines;
                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }
        if (cntFullLines > 0) {
            isFallingFinished = true;
            int newPoints = countPoints + cntFullLines + cntFullLines - 1;
            if (newPoints / POINTS_FOR_NEXT_LEVEL > countPoints / POINTS_FOR_NEXT_LEVEL) {
                level.setText(String.valueOf(Integer.parseInt(level.getText()) + 1));
                if (currentTimeout > INDENT_TIMEOUT) {
                    currentTimeout -= INDENT_TIMEOUT;
                }
                timer.setDelay(currentTimeout);
            }
            countPoints += cntFullLines + cntFullLines - 1;
            currentShape.setShape(Tetrominoes.EmptyShape);
            points.setText(String.valueOf(countPoints));
            repaint();
        }
    }

    /**
     * paint new position shape
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * cellHeight();
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetrominoes shapeName = shapeAt(j, BOARD_HEIGHT - i - 1);
                drawSquare(g,
                           j * cellWidth(),
                           boardTop + i * cellHeight(),
                           Shape.colors[shapeName.ordinal()]);
            }
        }

        if (currentShape.getShapeName() != Tetrominoes.EmptyShape) {
            for (int i = 0; i < 4; i++) {
                int x = currentX + currentShape.getX(i);
                int y = currentY - currentShape.getY(i);
                drawSquare(g,
                           x * cellWidth(),
                           boardTop + (BOARD_HEIGHT - y - 1) * cellHeight(),
                           Shape.colors[currentShape.getShapeName().ordinal()]);
            }
        }
    }

    /**
     * paint part of shape in <code>(X;Y)</code> coordinates with color <code>color</code>
     * @param g
     * @param x <code>X</code> coordinates for paint
     * @param y <code>Y</code> coordinates for paint
     * @param color color for this part
     */
    public void drawSquare(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x + 1, y + 1, cellWidth() - 2, cellHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + cellHeight() - 1, x, y);
        g.drawLine(x, y, x + cellWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + cellHeight() - 1, x + cellWidth() - 1, y + cellHeight() - 1);
        g.drawLine(x + cellWidth() - 1, y + cellHeight() - 1, x + cellWidth() - 1, y + 1);
    }

    /**
     * Listener for keyboard
     */
    private class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (!isStarted || currentShape.getShapeName() == Tetrominoes.EmptyShape) {
                System.out.println("KeyAdapter: " + isStarted + " " + currentShape.getShapeName());
                return;
            }
            int keyCode = e.getKeyCode();
            if (keyCode == 'p' || keyCode == 'P') {
                pause();
                return;
            }
            if (isPaused) {
                return;
            }
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    tryMove(currentShape, currentX - 1, currentY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(currentShape, currentX + 1, currentY);
                    break;
                case KeyEvent.VK_UP:
                    tryMove(currentShape.rotateLeft(), currentX, currentY);
                    break;
                case KeyEvent.VK_DOWN:
                    oneLineDown();
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    break;
            }
        }
    }

    /**
     * Class helps for limits winner name <code>JTextField</code>
     */
    private class CharLimitDocument extends PlainDocument {
        private int limit;

        public CharLimitDocument(int limit) {
            super();
            this.limit = limit;
        }

        public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {
            if (str == null) {
                return;
            }
            if (getLength() + str.length() <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }
}
