package com.example.gameexam;

public class Block {

    private int[][] shape;  // Форма блока (координаты его ячеек)
    private int x, y;  // Текущие координаты верхнего левого угла блока
    private TetrisView tetrisView;  // Ссылка на TetrisView
    private static final int[][][] SHAPES = {
            // I-образный блок
            {{0, 0}, {1, 0}, {2, 0}, {3, 0}},
            // O-образный блок
            {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
            // T-образный блок
            {{1, 0}, {0, 1}, {1, 1}, {2, 1}},
            // L-образный блок
            {{0, 0}, {0, 1}, {0, 2}, {1, 2}},
            // J-образный блок
            {{1, 0}, {1, 1}, {1, 2}, {0, 2}},
            // S-образный блок
            {{1, 0}, {2, 0}, {0, 1}, {1, 1}},
            // Z-образный блок
            {{0, 0}, {1, 0}, {1, 1}, {2, 1}}
    };

    public Block(int type, int startX, int startY, TetrisView tetrisView) {
        this.shape = SHAPES[type];
        this.x = startX;
        this.y = startY;
        this.tetrisView = tetrisView;  // Сохраняем ссылку на TetrisView
    }

    // Возвращаем координаты блока для отрисовки
    public int[][] getCoordinates() {
        int[][] coords = new int[shape.length][2];
        for (int i = 0; i < shape.length; i++) {
            coords[i][0] = shape[i][0] + x;
            coords[i][1] = shape[i][1] + y;
        }
        return coords;
    }

    // Перемещение блока вниз
    public void moveDown() {
        y++;
    }

    // Перемещение блока влево
    public void moveLeft() {
        x--;
    }

    // Перемещение блока вправо
    public void moveRight() {
        x++;
    }

    // Проверяем, можно ли переместить блок вниз
    public boolean canMoveDown() {
        for (int[] position : getCoordinates()) {
            if (position[1] >= TetrisView.NUM_ROWS - 1 || tetrisView.isCellOccupied(position[1] + 1, position[0])) {
                return false;
            }
        }
        return true;
    }

    // Проверяем, можно ли переместить блок влево
    public boolean canMoveLeft() {
        for (int[] position : getCoordinates()) {
            if (position[0] <= 0 || tetrisView.isCellOccupied(position[1], position[0] - 1)) {
                return false;
            }
        }
        return true;
    }

    // Проверяем, можно ли переместить блок вправо
    public boolean canMoveRight() {
        for (int[] position : getCoordinates()) {
            if (position[0] >= TetrisView.NUM_COLS - 1 || tetrisView.isCellOccupied(position[1], position[0] + 1)) {
                return false;
            }
        }
        return true;
    }
}
