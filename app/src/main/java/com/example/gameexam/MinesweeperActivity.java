package com.example.gameexam;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.IOException;

public class MinesweeperActivity extends AppCompatActivity {

    // Ключ для передачи настроек
    public static final String SETTINGS = "SETTINGS";

    // Объект настроек
    private Settings settings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_minesweeper_main); // Убедитесь, что этот layout существует и адаптирован для Minesweeper

        // Загружаем настройки или берем из сохраненного состояния
        if (savedInstanceState != null) {
            settings = savedInstanceState.getParcelable(SETTINGS);
        } else {
            try {
                settings = FileProcessing.loadSettings(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Обработчик для кнопки 'Play'
        findViewById(R.id.playButton_id).setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayActivity.class); // Запуск PlayActivity для Minesweeper
            intent.putExtra(SETTINGS, settings);
            startActivity(intent);
        });

        // Обработчик для кнопки 'Results'
        findViewById(R.id.resultsButton_id).setOnClickListener(v -> {
            Intent intent = new Intent(this, ResultsActivity.class); // Запуск ResultsActivity для Minesweeper
            startActivity(intent);
        });

        // Обработчик для кнопки 'Settings'
        findViewById(R.id.settingsButton_id).setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class); // Запуск SettingsActivity для Minesweeper
            intent.putExtra(SETTINGS, settings);
            startActivity(intent);
        });

        // Проверка текущей темы оформления
        checkTheme();
    }

    // Метод для проверки и установки темы
    private void checkTheme() {
        if (settings.getIsDarkMode() == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    // Сохранение состояния активности
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SETTINGS, settings);
    }

    // Восстановление настроек при возврате к активности
    @Override
    public void onResume() {
        super.onResume();
        try {
            settings = FileProcessing.loadSettings(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkTheme();
    }
}

