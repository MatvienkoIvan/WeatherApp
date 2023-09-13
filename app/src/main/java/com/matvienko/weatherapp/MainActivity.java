package com.matvienko.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;

    private TextView resultInfo;
    private TextView weatherPhraseTextView;
    private final String[] weatherPhrasesSunny = {
            "Сегодня солнце светит так ярко, что тигры могут полежать на теплой земле и расслабиться.",
            "Тепло и ясно, идеальное время для игры тигров в их виртуозные игры на свежем воздухе.",
            "Ясное небо и теплое солнышко – как раз то, что нужно молодым тигрятам для забавных страхов от собственной тени.",
            "Тигры обожают солнечные дни, когда их полоски блеснут особенно ярко!",
            "Сегодня погода такая же зажигательная, как игра бодрых тигров в лесу."
    };
    private final String[] weatherPhrasesRainy = {
            "Пасмурный день не помеха для игр тигров – они точно найдут, что делать во время дождя.",
            "Серые облака только подчеркивают яркие полоски тигров, делая их еще более выразительными.",
            "Дождь не останавливает настоящих тигров – они могут отправиться в лес на свои незабываемые приключения даже в такую погоду.",
            "Пасмурная погода создает таинственную атмосферу в лесу, словно зовет тигров на разведку новых мест.",
            "Дождливый день – прекрасная возможность для тигров проявить свою изобретательность и находчивость в играх."
    };
    private final String[] weatherPhrasesSnowy = {
            "Белоснежный покров снега как будто создан специально для скрытных движений великих тигров.",
            "Тихая зимняя погода приглашает тигров на забавные гонки по снежным полям.",
            "Снежная стихия не пугает тигров – они, напротив, находят в ней особое волшебство.",
            "Тигры с легкостью приспосабливаются к холоду и снегу, благодаря своим мощным телам и густой шерсти.",
            "Снег под лапами, зимний ветер в морде – такие дни тигры ощущают себя настоящими хозяевами зимнего леса."
    };
    private final String[] weatherPhrasesModerate = {
            "Сегодня на улице тепло, как объятия тигра, идеальное время для активных прогулок.",
            "Температура воздуха такая приятная, что даже тигры могут себе позволить небольшую дрему на солнышке.",
            "День не слишком жаркий, не слишком холодный – идеальное время для тигров охотиться на впечатления.",
            "Отличная погода для тигров, чтобы проявить свою грацию и силу в забавных играх и тренировках.",
            "Сегодня температура как раз в той золотой середине, когда даже тигры могут наслаждаться всеми прелестями природы."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button nextActivity = findViewById(R.id.nextActivity);

        weatherPhraseTextView = findViewById(R.id.weatherPhraseTextView);
        user_field =findViewById(R.id.user_field);
        Button mainButton = findViewById(R.id.mainButton);
        resultInfo = findViewById(R.id.resultInfo);
        mainButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_field.getText().toString().trim().equals(""))
                Toast.makeText(MainActivity.this, R.string.no_user_imput, Toast.LENGTH_LONG).show();
                else {
                    String city = user_field.getText().toString();
                    String key = "78c896f95e6e4763ef01d93f2621c3eb";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric";

                    new GetURLData().execute(url);
                }
            }
        });
        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WeatherNextDay.class);
                startActivity(intent);
            }
        });

    }

    private class GetURLData extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            resultInfo.setText("Ожидайте...");
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

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Ошибка URL"; // Вернуть сообщение об ошибке
            } catch (IOException e) {
                e.printStackTrace();
                return "Ошибка ввода-вывода"; // Вернуть сообщение об ошибке
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(result);
                double temperature = obj.getJSONObject("main").getDouble("temp");
                double feelsLike = obj.getJSONObject("main").getDouble("feels_like");
                double windSpeed = obj.getJSONObject("wind").getDouble("speed");
                double getRanny = obj.getJSONObject("wind").getDouble("speed");

                String temperatureText = "Температура: " + temperature + " °C";
                String feelsLikeText = "Ощущается как: " + feelsLike + " °C";
                String windSpeedText = "Скорость ветра: " + windSpeed + " км/ч";


                String finalResult = temperatureText + "\n" + feelsLikeText + "\n" + windSpeedText;
                resultInfo.setText(finalResult);

                String weatherPhrase = "";
                if (temperature >= 25) {
                    int randomIndex = new Random().nextInt(weatherPhrasesSunny.length);
                    String randomPhrase = weatherPhrasesSunny[randomIndex];
                    weatherPhrase = randomPhrase;
//                } else if (weatherCondition.equals("Rainy")) {
//                    int randomIndex = new Random().nextInt(weatherPhrasesRainy.length);
//                    String randomPhrase = weatherPhrasesRainy[randomIndex];
//                    weatherPhrase = randomPhrase;
                } else if (temperature <= 10) {
                    int randomIndex = new Random().nextInt(weatherPhrasesSnowy.length);
                    String randomPhrase = weatherPhrasesSnowy[randomIndex];
                    weatherPhrase = randomPhrase;
                } else if (temperature > 10 && temperature < 25) {
                    int randomIndex = new Random().nextInt(weatherPhrasesModerate.length);
                    String randomPhrase = weatherPhrasesModerate[randomIndex];
                    weatherPhrase = randomPhrase;
                }

                weatherPhraseTextView.setText(weatherPhrase);

            } catch (JSONException e) {
                e.printStackTrace();
                resultInfo.setText("Ошибка парсинга данных");
            }
        }
        }

    }

