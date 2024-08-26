package com.example.gameexam;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

// Results Activity class
public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_results);

        TextView tvResults = findViewById(R.id.results_id);
        Context context = getApplicationContext();
        String results = "";

        // Чтение результатов из results.txt
        try {
            results = FileProcessing.loadResults(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvResults.setText(results);

        // Обработчик для кнопки 'Clear'
        findViewById(R.id.clear_id).setOnClickListener(butPlay -> {
            try {
                FileProcessing.clearResults(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            finish();
        });
    }
}
