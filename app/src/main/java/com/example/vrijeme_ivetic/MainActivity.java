package com.example.vrijeme_ivetic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonGetWeather;
    private TextView textViewWeatherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        buttonGetWeather = findViewById(R.id.buttonGetWeather);
        textViewWeatherInfo = findViewById(R.id.textViewWeatherInfo);

        buttonGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                new GetWeatherTask().execute(city);
            }
        });
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            String apiKey = "4a3e1b810e78cb2ccaf0abf81c1b63b4";
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
//Huskvarha Buenos Aires Zagreb Ulm Rifle
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject json = new JSONObject(result);

                    // Extract weather information
                    String weatherDescription = json.getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description");

                    // Extract temperature in Kelvin
                    double temperatureKelvin = json.getJSONObject("main").getDouble("temp");

                    // Convert Kelvin to Celsius
                    double temperatureCelsius = temperatureKelvin - 273.15;

                    // Display temperature and weather description
                    String temperature = String.format("%.2f", temperatureCelsius) + " °C";
                    String weatherInfo = "Weather: " + weatherDescription + "\nTemperature: " + temperature;
                    textViewWeatherInfo.setText(weatherInfo);

                    // Display corresponding weather icon
                    int iconResource = getWeatherIconResource(weatherDescription);
                    if (iconResource != 0) {
                        ImageView weatherIcon = findViewById(R.id.weatherIcon);
                        weatherIcon.setImageResource(iconResource);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private int getWeatherIconResource(String weatherDescription) {
            // Map different weather descriptions to corresponding image resources
            switch (weatherDescription.toLowerCase()) {
                case "clear sky":
                    return R.drawable.clear_sky;
                case "few clouds":
                    return R.drawable.few_clouds;
                case "scattered clouds":
                    return R.drawable.scattered_clouds;
                case "broken clouds":
                    return R.drawable.scattered_clouds;
                case "shower rain":
                    return R.drawable.shower_rain;
                case "heavy inesity rain":
                    return R.drawable.shower_rain;
                case "rain":
                    return R.drawable.rain;
                case "light rain":
                    return R.drawable.rain;
                case "thunderstorm":
                    return R.drawable.thunderstorm;
                case "thunderstorm with light rain":
                    return R.drawable.thunderstorm;
                case "snow":
                    return R.drawable.snow;
                case "mist":
                    return R.drawable.mist;
                default:
                    return 0;
            }
        }
    }
}