package com.example.gameexam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

// Settings Activity class
public class SettingsActivity extends AppCompatActivity {

    // settings object
    private Settings settings;

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);
        Context context = getApplicationContext();

        // Проверяем состояние сохраненных настроек
        if (savedInstanceState != null) {
            settings = savedInstanceState.getParcelable(MinesweeperActivity.SETTINGS);
        } else {
            try {
                settings = FileProcessing.loadSettings(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Инициализация переключателя темной темы
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch isDarkModeOn = findViewById(R.id.switch_id);
        if (settings.getIsDarkMode() == 1) {
            isDarkModeOn.setChecked(true);
        }

        // Инициализация полей для ввода настроек
        EditText editCols = findViewById(R.id.columns_id);
        editCols.setText(String.format("%d", settings.getCols()));

        EditText editRows = findViewById(R.id.rows_id);
        editRows.setText(String.format("%d", settings.getRows()));

        EditText editMines = findViewById(R.id.numberOfMines_id);
        editMines.setText(String.format("%d", settings.getMinesNum()));

        TextView textError = findViewById(R.id.errorText_id);
        textError.setText("");

        // Обработчик для кнопки 'Save'
        findViewById(R.id.saveSettings_id).setOnClickListener(butPlay -> {
            settings.setCols(Integer.parseInt(editCols.getText().toString()));
            settings.setRows(Integer.parseInt(editRows.getText().toString()));
            settings.setMinesNum(Integer.parseInt(editMines.getText().toString()));

            // Проверяем корректность введенных значений
            int res = isEnteredValuesCorrect(settings.getRows(), settings.getCols(), settings.getMinesNum());

            if (res == 1) {
                textError.setText("Некорректное количество строк!");
            } else if (res == 2) {
                textError.setText("Некорректное количество столбцов!");
            } else if (res == 3) {
                textError.setText("Некорректное количество мин!");
            } else {
                textError.setText("");
                settings.setIsDarkMode(isDarkModeOn.isChecked() ? 1 : 0);

                // Сохранение настроек, если они корректны
                try {
                    FileProcessing.saveSettings(context, settings);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }

    // Проверка введенных значений
    private int isEnteredValuesCorrect(int rows, int cols, int mines) {
        if (rows <= 0 || rows > 25) {
            return 1;
        }

        if (cols <= 0 || cols > 25) {
            return 2;
        }

        if (mines <= 0 || mines > rows * cols / 3) {
            return 3;
        }

        return 0;
    }

    // Сохранение состояния окна
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MinesweeperActivity.SETTINGS, settings);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
