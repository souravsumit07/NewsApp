package com.example.newzapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button buttonBusiness, buttonEntertainment, buttonGeneral, buttonHealth, buttonScience, buttonSports, buttonTechnology;

    String apiKey = "1bc5ffa7af914ca18f1d31bb08b654d1";
    String baseUrl = "https://newsapi.org/v2/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonBusiness = findViewById(R.id.buttonBusiness);
        buttonEntertainment = findViewById(R.id.buttonEntertainment);
        buttonGeneral = findViewById(R.id.buttonGeneral);
        buttonHealth = findViewById(R.id.buttonHealth);
        buttonScience = findViewById(R.id.buttonScience);
        buttonSports = findViewById(R.id.buttonSports);
        buttonTechnology = findViewById(R.id.buttonTechnology);

        buttonBusiness.setOnClickListener(v -> fetchNews("business"));
        buttonEntertainment.setOnClickListener(v -> fetchNews("entertainment"));
        buttonGeneral.setOnClickListener(v -> fetchNews("general"));
        buttonHealth.setOnClickListener(v -> fetchNews("health"));
        buttonScience.setOnClickListener(v -> fetchNews("science"));
        buttonSports.setOnClickListener(v -> fetchNews("sports"));
        buttonTechnology.setOnClickListener(v -> fetchNews("technology"));

        fetchNews("general"); // Default fetch for general news
    }

    private void fetchNews(String category) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<NewsResponse> call = api.getTopHeadlines("us", apiKey, category);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Article> articles = response.body().getArticles();
                    Log.d("APIResponse", "Articles count: " + articles.size()); // Debug log for article count

                    if (articles != null && !articles.isEmpty()) {
                        NewsAdapter adapter = new NewsAdapter(MainActivity.this, articles);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.d("APIResponse", "No articles found");
                        Toast.makeText(MainActivity.this, "No articles found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("APIError", "Response not successful or body is null");
                    Toast.makeText(MainActivity.this, "Failed to load news", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("APIError", "API call failed: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to load news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
