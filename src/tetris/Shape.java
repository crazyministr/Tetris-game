package tetris;

import java.awt.*;
import java.lang.Math;
import java.util.Random;

public class Shape {
    private int coords[][];
    private Tetrominoes shapeName;

    enum Tetrominoes {
        EmptyShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, ReversedLShape
    }

    public Shape() {
        this(Tetrominoes.EmptyShape);
    }

    public Shape(Tetrominoes shapeName) {
        coords = new int[4][2];
        setShape(shapeName);
    }

    public final static Color[] colors = {
            new Color(0xD0D0D0),
            new Color(0x00FF00),
            new Color(0x78068D),
            new Color(0xFF0000),
            new Color(0x0000FF),
            new Color(0xCC66CC),
            new Color(0xFFED00),
            new Color(0xEC6600),
    };

    public final static int[][][] coordsTable = new int[][][] {
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}},    // Empty
            {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}}, // Z
            {{0, -1}, {0, 0}, {1, 0}, {1, 1}},   // S
            {{0, -1}, {0, 0}, {0, 1}, {0, 2}},   // Line
            {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},   // T
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}},    // Square
            {{1, -1}, {0, -1}, {0, 0}, {0, 1}},  // L
            {{-1, -1}, {0, -1}, {0, 0}, {0, 1}}, // reversed L
    };

    public void setShape(Tetrominoes shapeName) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsTable[shapeName.ordinal()][i][j];
            }
        }
        this.shapeName = shapeName;
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int getX(int index) {
        return coords[index][0];
    }

    public int getY(int index) {
        return coords[index][1];
    }

    public Tetrominoes getShapeName() {
        return shapeName;
    }

    public void setRandomShape() {
        Tetrominoes[] names = Tetrominoes.values();
        int rnd = Math.abs(new Random().nextInt());
        setShape(names[rnd % (names.length - 1) + 1]);
    }

    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    public Shape rotateLeft() {
        if (shapeName == Tetrominoes.SquareShape) {
            return this;
        }
        Shape result = new Shape(shapeName);
        for (int i = 0; i < 4; i++) {
            result.setX(i, getY(i));
            result.setY(i, -getX(i));
        }
        return result;
    }
}
