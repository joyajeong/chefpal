package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeLevelsActivity extends AppCompatActivity {

    private int recipeId;
    private static final String TAG = "RecipeLevelsActivity";
    TextView recipeTitle;
    RecipeSearchResult searchResult;
    public static List<RecipeInformationResult> recipeResults = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecipeRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciple_levels);
//        getRecipes();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get the RecyclerView and implement ClickListener
        recyclerView = findViewById(R.id.rvRecipes);
        RecipeRecyclerViewAdapter.ClickListener listener = new RecipeRecyclerViewAdapter.ClickListener() {
            @Override
            public void onRecipeClick(View view, String id) {
                Log.d(TAG, "recipe " + id + "clicked");
            }
        };

        //Created an adapter and supply the song data to be displayed
        adapter = new RecipeRecyclerViewAdapter(recipeResults, listener);
        recyclerView.setAdapter(adapter);

        //Set linear layout of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    //Gets recipes from API - 10
//    private void getRecipes() {
//        Call<RecipeSearchResult> call = RetrofitClient.getInstance().getMyApi()
//                //change maxReadyTime to adjust difficulty level
//                .recipeResults(null, null, "vegetarian", true, 15, SpoonacularClient.apiKey);
//
//        call.enqueue(new Callback<RecipeSearchResult>() {
//            @Override
//            public void onResponse(Call<RecipeSearchResult> call, Response<RecipeSearchResult> response) {
//                searchResult = response.body();
//                int sizeOfResult = searchResult.getResults().size();
//
//                if (response.body() != null) {
//                    for (int i = 0; i < sizeOfResult ; i++) {
//                        if (recipeResults.size() < 10) {
//                            getRecipeInfo(searchResult.getResults().get(i).getId());
//                        }
//                    }
//                } else {
//                    Log.d(TAG, "No results :(");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RecipeSearchResult> call, Throwable t) {
//                Log.i(TAG, "Failed API Call" + t.getCause());
//
//            }
//        });
//    }
//
//    private void getRecipeInfo(int id) {
//        Call<RecipeInformationResult> call = RetrofitClient.getInstance().getMyApi()
//                .recipeInformationResults(id, false, SpoonacularClient.apiKey);
//
//        call.enqueue(new Callback<RecipeInformationResult>() {
//            @Override
//            public void onResponse(Call<RecipeInformationResult> call, Response<RecipeInformationResult> response) {
//                RecipeInformationResult recipeInfo = response.body();
//                Log.i(TAG, "Source URL: " + recipeInfo.getSourceUrl());
//                Log.d(TAG, "Recipe Title: " + recipeInfo.getTitle());
//                addRecipes(recipeInfo);
//            }
//
//            @Override
//            public void onFailure(Call<RecipeInformationResult> call, Throwable t) {
//                Log.i(TAG, "Failed API Call" + t.getCause());
//
//            }
//        });
//    }
//
//    private void addRecipes(RecipeInformationResult result) {
//        Log.d(TAG, "Recipes are being added...");
//        if (result != null) {
//            recipeResults.add(new RecipeInformationResult(result.getId(), result.getTitle(), result.getImage(), result.getImageType(),
//                    result.getServings(), result.getReadyInMinutes(), result.getLicense(), result.getSourceName(), result.getSourceUrl(),
//                    result.getSpoonacularSourceUrl(), result.getAggregateLikes(), result.getSpoonacularScore(), result.getPricePerServing(),
//                    result.getAnalyzedInstructions(), result.getCheap(), result.getCreditsText(), result.getCuisines(), result.getDairyFree(),
//                    result.getDiets(), result.getGaps(), result.getGlutenFree(), result.getInstructions(), result.getKetogenic(),
//                    result.getLowFodmap(), result.getOccasions(), result.getSustainable(), result.getVegan(), result.getVegetarian(),
//                    result.getVeryHealthy(), result.getVeryPopular(), result.getWhole30(), result.getWeightWatcherSmartPoints(),
//                    result.getDishTypes(), result.getExtendedIngredients(), result.getSummary()));
//        } else {
//            Log.e(TAG, "Result is null");
//        }
//
//    }


}