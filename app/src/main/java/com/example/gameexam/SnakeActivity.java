package com.example.gameexam;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SnakeActivity extends AppCompatActivity {

    private static final int FPS = 60;
    private static final int SPEED = 25;

    private static final int STATUS_PAUSED = 1;
    private static final int STATUS_START = 2;
    private static final int STATUS_OVER = 3;
    private static final int STATUS_PLAYING = 4;

    private GameView mGameView;
    private TextView mGameStatusText;
    private TextView mGameScoreText;
    private Button mGameBtn;

    private int mGameStatus = STATUS_START;
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);

        // Инициализация элементов интерфейса
        mGameView = findViewById(R.id.game_view);
        mGameStatusText = findViewById(R.id.game_status);
        mGameBtn = findViewById(R.id.game_control_btn);
        mGameScoreText = findViewById(R.id.game_score);

        // Инициализация игры
        mGameView.init();
        mGameView.setGameScoreUpdatedListener(score -> mHandler.post(() -> mGameScoreText.setText("Score: " + score)));

        // Управление направлениями движения змейки
        findViewById(R.id.up_btn).setOnClickListener(v -> {
            if (mGameStatus == STATUS_PLAYING) {
                mGameView.setDirection(Direction.UP);
            }
        });
        findViewById(R.id.down_btn).setOnClickListener(v -> {
            if (mGameStatus == STATUS_PLAYING) {
                mGameView.setDirection(Direction.DOWN);
            }
        });
        findViewById(R.id.left_btn).setOnClickListener(v -> {
            if (mGameStatus == STATUS_PLAYING) {
                mGameView.setDirection(Direction.LEFT);
            }
        });
        findViewById(R.id.right_btn).setOnClickListener(v -> {
            if (mGameStatus == STATUS_PLAYING) {
                mGameView.setDirection(Direction.RIGHT);
            }
        });

        // Управление началом и остановкой игры
        mGameBtn.setOnClickListener(v -> {
            if (mGameStatus == STATUS_PLAYING) {
                setGameStatus(STATUS_PAUSED);
            } else {
                setGameStatus(STATUS_PLAYING);
            }
        });

        setGameStatus(STATUS_START);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGameStatus == STATUS_PLAYING) {
            setGameStatus(STATUS_PAUSED);
        }
    }

    private void setGameStatus(int gameStatus) {
        mGameStatus = gameStatus;
        mGameStatusText.setVisibility(View.VISIBLE);
        mGameBtn.setText("start");
        switch (gameStatus) {
            case STATUS_OVER:
                mGameStatusText.setText("GAME OVER");
                break;
            case STATUS_START:
                mGameView.newGame();
                mGameStatusText.setText("START GAME");
                break;
            case STATUS_PAUSED:
                mGameStatusText.setText("GAME PAUSED");
                break;
            case STATUS_PLAYING:
                if (gameStatus == STATUS_OVER) {
                    mGameView.newGame();
                }
                startGame();
                mGameStatusText.setVisibility(View.INVISIBLE);
                mGameBtn.setText("pause");
                break;
        }
    }

    private void startGame() {
        final int delay = 1000 / FPS;
        new Thread(() -> {
            int count = 0;
            while (!mGameView.isGameOver() && mGameStatus == STATUS_PLAYING) {
                try {
                    Thread.sleep(delay);
                    if (count % SPEED == 0) {
                        mGameView.next();
                        mHandler.post(mGameView::invalidate);
                    }
                    count++;
                } catch (InterruptedException ignored) {
                }
            }
            if (mGameView.isGameOver()) {
                mHandler.post(() -> setGameStatus(STATUS_OVER));
            }
        }).start();
    }
}