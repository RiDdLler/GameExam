package com.example.gameexam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SnakeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snake, container, false);

        // Находим изображение игры
        ImageView gameCover = view.findViewById(R.id.gameCover);

        // Устанавливаем слушатель нажатий на изображение
        gameCover.setOnClickListener(v -> {
            // Переход к SnakeActivity для запуска игры "Змейка"
            Intent intent = new Intent(getActivity(), SnakeActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
