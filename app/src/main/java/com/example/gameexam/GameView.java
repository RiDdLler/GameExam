package com.example.gameexam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.Random;

public class GameView extends View {

    private static final int GAME_SIZE_IN_PIXELS = 600;  // Задаем размер игрового поля напрямую
    private static final int MAP_SIZE = 20;
    private static final int START_X = 5;
    private static final int START_Y = 10;

    private final Point[][] mPoints = new Point[MAP_SIZE][MAP_SIZE];
    private final LinkedList<Point> mSnake = new LinkedList<>();
    private Direction mDir;

    private ScoreUpdatedListener mScoreUpdatedListener;
    private boolean mGameOver = false;

    private int mBoxSize;
    private int mBoxPadding;

    private final Paint mPaint = new Paint();

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        // Задаем размер игрового поля напрямую
        mBoxSize = GAME_SIZE_IN_PIXELS / MAP_SIZE;
        mBoxPadding = mBoxSize / 10;

        // Инициализация карты
        initMap();
    }

    public void setGameScoreUpdatedListener(ScoreUpdatedListener scoreUpdatedListener) {
        mScoreUpdatedListener = scoreUpdatedListener;
    }

    private void initMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                mPoints[i][j] = new Point(j, i);
            }
        }
        mSnake.clear();
        for (int i = 0; i < 3; i++) {
            Point point = getPoint(START_X + i, START_Y);
            point.type = PointType.SNAKE;
            mSnake.addFirst(point);
        }
        randomApple();
    }

    private void randomApple() {
        Random random = new Random();
        while (true) {
            Point point = getPoint(random.nextInt(MAP_SIZE), random.nextInt(MAP_SIZE));
            if (point.type == PointType.EMPTY) {
                point.type = PointType.APPLE;
                break;
            }
        }
    }

    private Point getPoint(int x, int y) {
        return mPoints[y][x];
    }

    public void newGame() {
        mGameOver = false;
        mDir = Direction.RIGHT;
        initMap();
        updateScore();
    }

    public void updateScore() {
        if (mScoreUpdatedListener != null) {
            int score = mSnake.size() - 3;
            mScoreUpdatedListener.onScoreUpdated(score);
        }
    }

    public void setDirection(Direction dir) {
        if ((dir == Direction.LEFT || dir == Direction.RIGHT) &&
                (mDir == Direction.LEFT || mDir == Direction.RIGHT)) {
            return;
        }
        if ((dir == Direction.UP || dir == Direction.DOWN) &&
                (mDir == Direction.UP || mDir == Direction.DOWN)) {
            return;
        }
        mDir = dir;
    }

    public void next() {
        Point first = mSnake.getFirst();
        Log.d("GameView", "first: " + first.x + " " + first.y);
        Point next = getNext(first);
        Log.d("GameView", "next: " + next.x + " " + next.y);

        switch (next.type) {
            case EMPTY:
                next.type = PointType.SNAKE;
                mSnake.addFirst(next);
                mSnake.getLast().type = PointType.EMPTY;
                mSnake.removeLast();
                break;
            case APPLE:
                next.type = PointType.SNAKE;
                mSnake.addFirst(next);
                randomApple();
                updateScore();
                break;
            case SNAKE:
                mGameOver = true;
                break;
        }
    }

    private Point getNext(Point point) {
        int x = point.x;
        int y = point.y;

        switch (mDir) {
            case UP:
                y = y == 0 ? MAP_SIZE - 1 : y - 1;
                break;
            case DOWN:
                y = y == MAP_SIZE - 1 ? 0 : y + 1;
                break;
            case LEFT:
                x = x == 0 ? MAP_SIZE - 1 : x - 1;
                break;
            case RIGHT:
                x = x == MAP_SIZE - 1 ? 0 : x + 1;
                break;
        }
        return getPoint(x, y);
    }

    public boolean isGameOver() {
        return mGameOver;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Рисуем игровое поле
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                int left = mBoxSize * x;
                int right = left + mBoxSize;
                int top = mBoxSize * y;
                int bottom = top + mBoxSize;

                switch (getPoint(x, y).type) {
                    case APPLE:
                        mPaint.setColor(Color.RED);
                        break;
                    case SNAKE:
                        mPaint.setColor(Color.BLACK);
                        canvas.drawRect(left, top, right, bottom, mPaint);
                        mPaint.setColor(Color.WHITE);
                        left += mBoxPadding;
                        right -= mBoxPadding;
                        top += mBoxPadding;
                        bottom -= mBoxPadding;
                        break;
                    case EMPTY:
                        mPaint.setColor(Color.BLACK);
                        break;
                }
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
