package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeLevelsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvDifficultyLevel;
    private static RecipeRecyclerViewAdapter adapter;
    private static final String TAG = "RecipeLevelsListActivity";
    //TODO figure out this variable thing
    public static List<RecipeInformationResult> recipes = new ArrayList<>();
    private List<RecipeInformationResult> initialRecipes = new ArrayList<>();

    private String difficultyLevel, recipeType;
    public static int NUM_RESULTS = 3;
    public static int EASY_LEVEL = 15;
    public static int MED_LEVEL = 30;
    public static int HARD_LEVEL = 45;
    RecipeSearchResult searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_levels_list);

        recyclerView = findViewById(R.id.rvRecipes);
        tvDifficultyLevel = findViewById(R.id.tvDifficultyLevel);

        Bundle bundle = getIntent().getExtras();
        difficultyLevel = bundle.getString("DIFFICULTY_LEVEL");
        recipeType = bundle.getString("RECIPE_TYPE");
        Log.d(TAG, difficultyLevel);
        Log.d(TAG, recipeType);


        //TODO figure out a better way to do the switch statements
        switch(difficultyLevel) {
            case "EASY":
                tvDifficultyLevel.setText("Easy");
                break;
            case "MED":
                tvDifficultyLevel.setText("Medium");
                break;
            case "HARD":
                tvDifficultyLevel.setText("Hard");
                break;
        }

        switch(recipeType) {
            case "VEG":
                switch(difficultyLevel) {
                    case "EASY":
                        getVegRecipes(EASY_LEVEL);
                        break;
                    case "MED":
                        getVegRecipes(MED_LEVEL);
                        break;
                    case "HARD":
                        getVegRecipes(HARD_LEVEL);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get the RecyclerView and implement ClickListener
        setRecyclerView();
    }

    private void setRecyclerView() {
        Log.d(TAG, "in setRecyclerView");
        RecipeRecyclerViewAdapter.ClickListener listener = new RecipeRecyclerViewAdapter.ClickListener() {
            @Override
            public void onRecipeClick(View view, String id) {
                Log.d(TAG, "recipe " + id + " clicked");
                Intent intent = new Intent(RecipeLevelsListActivity.this, RecipeDetailActivity.class);
                intent.putExtra("RECIPE_ID", id);
                startActivity(intent);
            }
        };
        //Created an adapter and supply the song data to be displayed
        adapter = new RecipeRecyclerViewAdapter(initialRecipes, listener);
        recyclerView.setAdapter(adapter);

        //Set linear layout of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getVegRecipes(int level) {
        getRecipes(null, null, "vegetarian", level);
    }

    //Gets recipes from API - Preparing the recipes for the RecyclerView
    private void getRecipes(String query, String cuisine, String diet, int maxReadyTime) {
        Call<RecipeSearchResult> call = RetrofitClient.getInstance().getMyApi()
                //change maxReadyTime to adjust difficulty level
                .recipeResults(query, cuisine, diet, true, maxReadyTime, "time", SpoonacularClient.apiKey);

        call.enqueue(new Callback<RecipeSearchResult>() {
            @Override
            public void onResponse(Call<RecipeSearchResult> call, Response<RecipeSearchResult> response) {
                searchResult = response.body();

                if (response.body() != null && recipes.size() <= NUM_RESULTS) {
                    for (int i = 0; i < NUM_RESULTS ; i++) {
                        Log.d(TAG, "Current index: " + i);
                        getRecipeInfo(searchResult.getResults().get(i).getId());
                    }
                } else {
                    Log.d(TAG, "No results :(");
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResult> call, Throwable t) {
                Log.i(TAG, "Failed API Call" + t.getCause());

            }
        });
    }

    private void getRecipeInfo(int id) {
        RecipeInformationResult recipeInfo;
        Call<RecipeInformationResult> call = RetrofitClient.getInstance().getMyApi()
                .recipeInformationResults(id, false, SpoonacularClient.apiKey);

        call.enqueue(new Callback<RecipeInformationResult>() {
            @Override
            public void onResponse(Call<RecipeInformationResult> call, Response<RecipeInformationResult> response) {
                RecipeInformationResult recipeInfo = response.body();
                //check if response body is null
                Log.i(TAG, "Source URL: " + recipeInfo.getSourceUrl());
                Log.d(TAG, "Recipe Title: " + recipeInfo.getTitle());
                Log.d(TAG, "Recipe time: " + recipeInfo.getReadyInMinutes());
                addRecipes(recipeInfo);
            }

            @Override
            public void onFailure(Call<RecipeInformationResult> call, Throwable t) {
                Log.i(TAG, "Failed API Call" + t.getCause());

            }
        });
    }

   private void addRecipes(RecipeInformationResult result) {
        if (result != null && noDuplicateRecipes(result, recipes)) {
            Log.d(TAG, "Recipes are being added...");
            recipes.add(new RecipeInformationResult(result.getId(), result.getTitle(), result.getImage(), result.getImageType(),
                    result.getServings(), result.getReadyInMinutes(), result.getLicense(), result.getSourceName(), result.getSourceUrl(),
                    result.getSpoonacularSourceUrl(), result.getAggregateLikes(), result.getSpoonacularScore(), result.getPricePerServing(),
                    result.getAnalyzedInstructions(), result.getCheap(), result.getCreditsText(), result.getCuisines(), result.getDairyFree(),
                    result.getDiets(), result.getGaps(), result.getGlutenFree(), result.getInstructions(), result.getKetogenic(),
                    result.getLowFodmap(), result.getOccasions(), result.getSustainable(), result.getVegan(), result.getVegetarian(),
                    result.getVeryHealthy(), result.getVeryPopular(), result.getWhole30(), result.getWeightWatcherSmartPoints(),
                    result.getDishTypes(), result.getExtendedIngredients(), result.getSummary()));
            Log.d(TAG, "Number of recipes: " + recipes.size());
        } else {
            Log.e(TAG, "Result is null");
        }

        if (recipes.size() == NUM_RESULTS) {
            Log.d(TAG, "Added all the recipes to the RecyclerView");
            adapter.updateRecipeList(recipes);
        }

    }

    public static boolean noDuplicateRecipes(RecipeInformationResult recipe, List<RecipeInformationResult> list) {
        for (RecipeInformationResult r : list) {
            if (r.getId().equals(recipe.getId())) {
                Log.d(TAG, "Trying to add an existing recipe!");
                return false;
            }
        }
        return true;
    }

}