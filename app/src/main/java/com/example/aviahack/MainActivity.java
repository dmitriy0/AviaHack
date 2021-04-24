package com.example.aviahack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeechService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Url;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.ResponseBody;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URL url = null;

        try {
            url = new URL("https://api.rasp.yandex.net/v3.0/schedule/?apikey=5e4e55ee-dd0a-4a0b-986e-8130b61d4134&station=SVO&transport_types=plane&system=iata");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        final OkHttpClient client = builder.build();
        Service service = new Service() {
            @Override
            public Call<ResponseBody> get(String url) {
                return null;
            }

        };

        service = new Retrofit.Builder().baseUrl(url).client(client).build().create(MainActivity.Service.class);
        final Service finalService = service;

        Call<okhttp3.ResponseBody> req = finalService.get("");

        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String res = null;
                try {
                    assert response.body() != null;
                    res = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), res + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });



    }
    interface Service {
        @GET
        Call<ResponseBody> get(@Url String url);
    }

}

