package tetris;

import javax.smartcardio.TerminalFactory;
import java.awt.*;
import java.util.Random;
import java.lang.Math;

public class Shape {
    private int coords[][];
    private Tetrominoes pieceShape;

    enum Tetrominoes {
        NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape
    }

    public Shape() {
        coords = new int[4][2];
        setShape(Tetrominoes.NoShape);
    }

    static Color[] colors = {
            new Color(0x000000),
            new Color(0x00FF00),
            new Color(0x78068D),
            new Color(0xFF0000),
            new Color(0x0000FF),
            new Color(0xCC66CC),
            new Color(0xFFED00),
            new Color(0xEC6600),
    };

    public void setShape(Tetrominoes shape) {
        int[][][] coordsTable = new int[][][]{
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
            {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}}, // S
            {{0, -1}, {0, 0}, {1, 0}, {1, 1}},   // Z
            {{0, -1}, {0, 0}, {0, 1}, {0, 2}},   // Line
            {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},   // T
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}},    // Square
            {{1, -1}, {0, -1}, {0, 0}, {0, 1}},  // L
            {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},  // reverse L
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int x(int index) {
        return coords[index][0];
    }

    public int y(int index) {
        return coords[index][1];
    }

    public Tetrominoes getShape() {
        return pieceShape;
    }

    public void setRandomShape() {
        Tetrominoes[] values = Tetrominoes.values();
        int rnd = Math.abs(new Random().nextInt());
        setShape(values[rnd % (values.length - 1) + 1]);
    }

    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    public Shape rotateLeft() {
        if (pieceShape == Tetrominoes.SquareShape) {
            return this;
        }
        Shape result = new Shape();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }
}
