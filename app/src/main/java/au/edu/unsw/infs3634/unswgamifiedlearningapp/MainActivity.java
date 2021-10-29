package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //test push
    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "users").build();

        UserDao userDao = db.userDao();

        //Not sure if we should just use database in main thread or create different threads
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            userDao.insertAll(User.userData());
            Log.i("Added user", userDao.findById("joyaj").firstName);
        });

        getRecipes();

    }

    //Gets recipes from API - just a test
    private void getRecipes() {
        Call<RecipeSearchResult> call = RetrofitClient.getInstance().getMyApi()
                .recipeResults("pasta", "italian", null, true, 10, SpoonacularClient.apiKey);

        call.enqueue(new Callback<RecipeSearchResult>() {
            @Override
            public void onResponse(Call<RecipeSearchResult> call, Response<RecipeSearchResult> response) {
                RecipeSearchResult recipes = response.body();
                recipeId = recipes.getResults().get(0).getId();
                getRecipeInfo(recipeId);
                Log.i("Main Activity API Test", recipes.getResults().get(0).getTitle());
            }

            @Override
            public void onFailure(Call<RecipeSearchResult> call, Throwable t) {
                Log.i("Main Activity API Test", "Failed" + t.getCause());

            }
        });
    }

    private void getRecipeInfo(int id) {
        Call<RecipeInformationResult> call = RetrofitClient.getInstance().getMyApi()
                .recipeInformationResults(id, SpoonacularClient.apiKey);

        call.enqueue(new Callback<RecipeInformationResult>() {
            @Override
            public void onResponse(Call<RecipeInformationResult> call, Response<RecipeInformationResult> response) {
                RecipeInformationResult recipeInfo = response.body();
                Log.i("Main Activity API Test", recipeInfo.getSourceUrl());
            }

            @Override
            public void onFailure(Call<RecipeInformationResult> call, Throwable t) {
                Log.i("Main Activity API Test", "Failed" + t.getCause());

            }
        });
    }
}