package com.example.aviahack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class FindFlight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_flight);

        final EditText flightNumber = findViewById(R.id.flightNumber);
        final EditText flightTime = findViewById(R.id.flightTime);
        final EditText flightTo = findViewById(R.id.flightTo);
        final TextView text = findViewById(R.id.text);
        Button find = findViewById(R.id.button2);

        URL url = null;

        try {
            url = new URL("https://api.rasp.yandex.net/v3.0/schedule/?apikey=5e4e55ee-dd0a-4a0b-986e-8130b61d4134&station=SVO&transport_types=plane&system=iata");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        final URL finalUrl = url;
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                service = new Retrofit.Builder().baseUrl(finalUrl).client(client).build().create(FindFlight.Service.class);
                final Service finalService = service;

                final Call<okhttp3.ResponseBody> req = finalService.get("");

                req.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String res = null;
                        JSONArray result = null;
                        try {
                            assert response.body() != null;
                            res = response.body().string();
                            final JSONObject jsonObject = new JSONObject(res);
                            result = jsonObject.getJSONArray("schedule");
                            JSONObject flight = null;

                            for (int i=0;i<result.length();i++){

                                flight = new JSONObject(result.get(i).toString());
                                if (flight.getJSONObject("thread").getString("number").contentEquals(flightNumber.getText())){
                                    text.setText(flight.getJSONObject("thread").getString("uid"));
                                }
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("ERROR", t.getMessage());
                    }
                });
            }
        });





    }
    interface Service {
        @GET
        Call<ResponseBody> get(@Url String url);
    }
}
