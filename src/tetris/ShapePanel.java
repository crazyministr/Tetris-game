package tetris;

import java.awt.*;
import javax.swing.*;
import tetris.Shape.Tetrominoes;

public class ShapePanel extends JPanel {
    private final int BOARD_WIDTH = 7;
    private final int BOARD_HEIGHT = 7;

    private Shape shape;
    private int currentX = 0;
    private int currentY = 0;
    private Shape.Tetrominoes[] board;

    public ShapePanel() {
        setOpaque(false);
//        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 3));
        board = new Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
        initBoard();
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        currentX = BOARD_WIDTH / 2;
        currentY = BOARD_HEIGHT - 2 + shape.minY();
        initBoard();
        repaint();
    }

    private void initBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Shape.Tetrominoes.EmptyShape;
        }
    }

    int cellWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH; // 198 / 6 = 33
    }

    int cellHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT; // 66 / 6 = 11
    }

    Tetrominoes shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * cellHeight();
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Shape.Tetrominoes shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                drawSquare(g,
                           j * cellWidth(),
                           boardTop + i * cellHeight(),
                           Shape.colors[shape.ordinal()]);
            }
        }

        if (shape.getShapeName() == Shape.Tetrominoes.EmptyShape) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            int x = currentX + shape.getX(i);
            int y = currentY - shape.getY(i);
            drawSquare(g,
                       x * cellWidth(),
                       boardTop + (BOARD_HEIGHT - y - 1) * cellHeight(),
                       Shape.colors[shape.getShapeName().ordinal()]);
        }
    }

    private void drawSquare(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x + 1, y + 1, cellWidth() - 2, cellHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + cellHeight() - 1, x, y);
        g.drawLine(x, y, x + cellWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + cellHeight() - 1, x + cellWidth() - 1, y + cellHeight() - 1);
        g.drawLine(x + cellWidth() - 1, y + cellHeight() - 1, x + cellWidth() - 1, y + 1);
    }
}
