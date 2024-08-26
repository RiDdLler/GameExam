package com.example.gameexam;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

// Класс для обработки файлов
public class FileProcessing {

    // Имена файлов
    public static final String SETTINGS_FILENAME = "settings.txt";
    public static final String RESULTS_FILENAME = "results.txt";

    // Загрузка настроек из файла settings.txt
    public static Settings loadSettings(Context context) throws IOException {
        Settings settings = null;
        File file = context.getFileStreamPath(SETTINGS_FILENAME);

        // Если приложение запущено впервые, настройки не загружаются
        if (!file.exists()) {
            settings = new Settings(10, 10, 15, 0);  // значения по умолчанию
            return settings;
        }

        // Иначе читаем настройки из файла
        InputStream is = context.openFileInput(SETTINGS_FILENAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String s;
        while ((s = reader.readLine()) != null) {
            Log.d("FileProcessing", "Чтение строки: " + s.length());
            if (s.length() == 0) {
                break;
            }
            String[] valuesSettings = s.split(",");
            settings = new Settings(
                    Integer.parseInt(valuesSettings[0]),
                    Integer.parseInt(valuesSettings[1]),
                    Integer.parseInt(valuesSettings[2]),
                    Byte.parseByte(valuesSettings[3])
            );
        }
        return settings;
    }

    // Сохранение настроек в файл settings.txt
    public static void saveSettings(Context context, Settings settings) throws IOException {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(SETTINGS_FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(
                    settings.getCols() + "," +
                            settings.getRows() + "," +
                            settings.getMinesNum() + "," +
                            settings.getIsDarkMode()
            );
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("FileProcessing", "Ошибка записи файла: " + e);
        }
    }

    // Добавление результата в файл results.txt
    public static void saveResult(Context context, String resultStr) throws IOException {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(RESULTS_FILENAME, Context.MODE_APPEND));
            outputStreamWriter.write(resultStr + "\n");
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Загрузка результатов из файла results.txt
    public static String loadResults(Context context) throws IOException {
        File file = context.getFileStreamPath(RESULTS_FILENAME);

        // Если приложение запущено впервые, результаты не загружаются
        if (!file.exists()) {
            return "";
        }

        // Иначе читаем результаты из файла
        InputStream is = context.openFileInput(RESULTS_FILENAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder resultStr = new StringBuilder();
        String s;
        while ((s = reader.readLine()) != null) {
            Log.d("FileProcessing", "Чтение строки: " + s.length());
            resultStr.append(s).append("\n");
            if (s.length() == 0) {
                break;
            }
        }
        Log.d("FileProcessing", "Содержимое файла: " + resultStr.toString());
        return resultStr.toString();
    }

    // Очистка результатов в файле results.txt
    public static void clearResults(Context context) throws IOException {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(RESULTS_FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write("");  // Очистка файла
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
