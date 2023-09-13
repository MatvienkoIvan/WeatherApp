package com.matvienko.weatherapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class WeatherNextDay extends AppCompatActivity {
    private Button mur_btn;
    private MediaPlayer mediaPlayer; // Перемещаем MediaPlayer в поле класса

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_next_day);

        mur_btn = findViewById(R.id.mur_btn);
        mediaPlayer = MediaPlayer.create(this, R.raw.kotmyaukaet);

        Button back_activity = findViewById(R.id.back_btn);

        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherNextDay.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mur_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}






