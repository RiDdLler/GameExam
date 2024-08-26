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

public class MinesweeperFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minesweeper, container, false);

        ImageView gameCover = view.findViewById(R.id.gameCover);
        gameCover.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PlayActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
