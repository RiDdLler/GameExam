package com.example.gameexam;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class TetrisActivity extends AppCompatActivity {

    private TetrisView tetrisView;
    private Handler handler = new Handler();
    private final int FALL_DELAY = 1000;  // 1 секунда

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Устанавливаем кастомную вью для игры
        tetrisView = new TetrisView(this);
        setContentView(tetrisView);

        // Начинаем игру, блоки падают каждые 1 секунду
        startFallingBlocks();
    }

    private void startFallingBlocks() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tetrisView.updateFallingBlock(); // Обновляем положение блока
                tetrisView.invalidate();  // Перерисовываем экран
                handler.postDelayed(this, FALL_DELAY);  // Повторяем каждые 1 секунду
            }
        }, FALL_DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);  // Останавливаем игру при паузе
    }

    @Override
    protected void onResume() {
        super.onResume();
        startFallingBlocks();  // Возобновляем игру при возвращении
    }
}
