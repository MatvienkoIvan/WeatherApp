package com.matvienko.weatherapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.GetChars;
import android.text.Layout;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherNextDay extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button weatherFourDays;
    private EditText userField ;
    private TextView weatherDayFirst;  //Необходимо добавить поле для вывода.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_next_day);
        weatherFourDays = findViewById(R.id.weather_four_days);
        Button mur_btn = findViewById(R.id.mur_btn);
        userField = findViewById(R.id.user_field_next);
        mediaPlayer = MediaPlayer.create(this, R.raw.kotmyaukaet);
        Button back_activity = findViewById(R.id.back_btn);
        weatherDayFirst = findViewById(R.id.dayOne);

        weatherFourDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userField.getText().toString().trim().equals(""))
                    Toast.makeText(WeatherNextDay.this, "Введите город", Toast.LENGTH_LONG).show();
                else {
// Получите текущее время в миллисекундах
            long currentTimeMillis = System.currentTimeMillis();
// Преобразуйте время в формат Unix Timestamp (в секундах)
            long unixTimestamp = currentTimeMillis / 1000L;
// Преобразуйте Unix Timestamp в строку
             String timestampString = String.valueOf(unixTimestamp);
// Создайте объект SimpleDateFormat для форматирования даты и времени
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
// Преобразуйте текущее время в формат строки
            String currentDatetime = dateFormat.format(new Date(currentTimeMillis));
            String city = userField.getText().toString();
            String key = "78c896f95e6e4763ef01d93f2621c3eb";
// Добавьте текущее время в ваш URL
            String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + key + "&dt=" + timestampString + "&units=metric";

            new GetURLData().execute(url);

//                    String city = userField.getText().toString();
//                    String key = "78c896f95e6e4763ef01d93f2621c3eb";
//                    String url = "https://api.openweathermap.org/data/2.5/forecast?q="+ city+ "&appid=" + key + "dt="+  +"&units=metric";
//                    new GetURLData().execute(url);
                }
            }
        });

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
    private class GetURLData extends AsyncTask <String, String, String> {
        protected void onPreExecute (){
        super.onPreExecute();
            weatherDayFirst.setText ("Ожидайте....");
    }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line= reader.readLine()) !=null);
                    buffer.append(line).append("\n");
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Ошибка URL";

            } catch (IOException e) {
                e.printStackTrace();
                return "Ошибка ввода-вывода";
            } finally {
                if (connection !=null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        protected void onPostExecute (String result) {
            super.onPreExecute();
            try {
                JSONObject obj = new JSONObject(result);
//                double time = obj.getJSONObject("dt").
                double temperature = obj.getJSONObject("main").getDouble("temp");



                String temperatureText = "Температура: " + temperature + " °C";


                String finalResult = temperatureText ;
                weatherDayFirst.setText(finalResult);

            } catch (JSONException e) {
                e.printStackTrace();
                weatherDayFirst.setText("Ошибка парсинга данных");
            }
        }

    }
}






