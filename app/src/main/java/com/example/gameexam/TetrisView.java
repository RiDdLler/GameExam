package com.example.gameexam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.Random;

public class TetrisView extends View {

    public static final int NUM_ROWS = 20;
    public static final int NUM_COLS = 10;
    private static final int BLOCK_SIZE = 60;
    private Block currentBlock;
    private Paint paint;
    private boolean[][] gameField;
    private int score = 0;
    private Random random;  // Добавляем Random

    // Конструктор для программного создания View
    public TetrisView(Context context) {
        super(context);
        init();
    }

    // Конструктор для создания View через XML
    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Общая инициализация для обоих конструкторов
    private void init() {
        paint = new Paint();
        gameField = new boolean[NUM_ROWS][NUM_COLS];  // Инициализируем пустое поле
        random = new Random();  // Инициализация Random
        createNewBlock();  // Создаем первый блок
    }

    // Метод для создания нового блока
    private void createNewBlock() {
        int type = random.nextInt(7);  // Генерация случайного типа блока (7 типов блоков)
        currentBlock = new Block(type, NUM_COLS / 2, 0, this);  // Создаем новый блок
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Рисуем фон
        canvas.drawColor(Color.BLACK);

        // Рисуем белую границу вокруг поля
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        canvas.drawRect(0, 0, NUM_COLS * BLOCK_SIZE, NUM_ROWS * BLOCK_SIZE, paint);

        // Рисуем блоки на игровом поле
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (gameField[row][col]) {
                    canvas.drawRect(
                            col * BLOCK_SIZE,
                            row * BLOCK_SIZE,
                            (col + 1) * BLOCK_SIZE,
                            (row + 1) * BLOCK_SIZE,
                            paint
                    );
                }
            }
        }

        // Рисуем текущий падающий блок
        paint.setColor(Color.WHITE);
        if (currentBlock != null) {
            for (int[] position : currentBlock.getCoordinates()) {
                canvas.drawRect(
                        position[0] * BLOCK_SIZE,
                        position[1] * BLOCK_SIZE,
                        (position[0] + 1) * BLOCK_SIZE,
                        (position[1] + 1) * BLOCK_SIZE,
                        paint
                );
            }
        }

        // Отображение счета
        paint.setTextSize(60);
        paint.setColor(Color.WHITE);
        canvas.drawText("Score: " + score, 10, NUM_ROWS * BLOCK_SIZE + 100, paint);
    }

    // Метод для обновления позиции падающего блока
    public void updateFallingBlock() {
        if (currentBlock != null && currentBlock.canMoveDown()) {
            currentBlock.moveDown();
        } else {
            if (currentBlock != null) {
                fixBlockOnField();
                clearFullRows();
            }
            createNewBlock();
        }
        invalidate();  // Перерисовываем экран
    }

    // Фиксация текущего блока на игровом поле
    private void fixBlockOnField() {
        for (int[] position : currentBlock.getCoordinates()) {
            gameField[position[1]][position[0]] = true;
        }
    }

    // Проверка и удаление заполненных рядов
    private void clearFullRows() {
        for (int row = 0; row < NUM_ROWS; row++) {
            boolean fullRow = true;
            for (int col = 0; col < NUM_COLS; col++) {
                if (!gameField[row][col]) {
                    fullRow = false;
                    break;
                }
            }

            if (fullRow) {
                for (int r = row; r > 0; r--) {
                    for (int col = 0; col < NUM_COLS; col++) {
                        gameField[r][col] = gameField[r - 1][col];
                    }
                }

                for (int col = 0; col < NUM_COLS; col++) {
                    gameField[0][col] = false;
                }

                score += 100;  // Добавляем очки за полный ряд
            }
        }
    }

    // Проверка, занята ли клетка
    public boolean isCellOccupied(int row, int col) {
        return row >= 0 && row < NUM_ROWS && col >= 0 && col < NUM_COLS && gameField[row][col];
    }

    // Обработка касаний для перемещения блока
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && currentBlock != null) {
            float touchX = event.getX();
            if (touchX < getWidth() / 2 && currentBlock.canMoveLeft()) {
                currentBlock.moveLeft();
            } else if (touchX >= getWidth() / 2 && currentBlock.canMoveRight()) {
                currentBlock.moveRight();
            }
            invalidate();
            return true;
        }
        return false;
    }
}
