package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeLevelsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvDifficultyLevel;
    private static RecipeRecyclerViewAdapter adapter;
    private static final String TAG = "RecipeLevelsListActivity";
    //TODO figure out this variable thing
    private List<RecipeInformationResult> recipes = new ArrayList<>();
    private List<RecipeInformationResult> initialRecipes = new ArrayList<>();
    private RecipesDao recipesDao;
    private RecipeTypeDao recipeTypeDao;
    private String difficultyLevel, recipeType;
    public static int NUM_RESULTS = 5;
    public static int EASY_LEVEL = 15;
    public static int MED_LEVEL = 30;
    public static int HARD_LEVEL = 45;
    private int level;
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
        setRecyclerView();

        //TODO figure out a better way to do the switch statements
        switch(difficultyLevel) {
            case "EASY":
                level = 15;
                tvDifficultyLevel.setText("Easy");
                break;
            case "MED":
                level = 30;
                tvDifficultyLevel.setText("Medium");
                break;
            case "HARD":
                level = 45;
                tvDifficultyLevel.setText("Hard");
                break;
        }

        //Recipes database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "recipes").fallbackToDestructiveMigration().build();
        recipesDao = db.recipesDao();

        //RecipeType database
        AppDatabase dbRT = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "recipeType").fallbackToDestructiveMigration().build();
        recipeTypeDao = dbRT.recipeTypeDao();

        //TODO: make a general method for inside the run()
        switch(recipeType) {
            case "VEG":
                switch(difficultyLevel) {
                    case "EASY":
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                //Gets the relevant recipe IDs (by recipe type and difficulty level)
                                List<Integer> recipeIds = recipeTypeDao.getAllRecipeIdByTypeLevel("VEG", "EASY");
                                Log.d(TAG, "number of relevant recipes: " + recipeIds.size());
                                //If there are no relevant recipes in the database currently, then get the recipes
                                //using the SpoonacularApi
                                if (recipeIds.size() == 0 || recipeIds == null) {
                                    Log.d(TAG, "no relevant recipes in the database");
                                    getVegRecipes(EASY_LEVEL);
                                } else {
                                    //If there are relevant recipes, then fetch them from the database and
                                    //update the adapters for the recycler view
                                    Log.d(TAG, "found some relevant recipes in the database");
                                    for (Integer id : recipeIds) {
                                        if (noDuplicateRecipes(id, recipes)) {
                                            recipes.add(recipesDao.findById(id));
                                        }
                                    }
                                    setAdapters();
                                }
                            }
                        });
                        break;
                    case "MED":
                        getVegRecipes(MED_LEVEL);
                        break;
                    case "HARD":
                        getVegRecipes(HARD_LEVEL);
                        break;
                }
                break;
            case "PESC":
                switch(difficultyLevel) {
                    case "EASY":
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                //Gets the relevant recipe IDs (by recipe type and difficulty level)
                                List<Integer> recipeIds = recipeTypeDao.getAllRecipeIdByTypeLevel("PESC", "EASY");
                                //If there are no relevant recipes in the database currently, then get the recipes
                                //using the SpoonacularApi
                                if (recipeIds.size() == 0 || recipeIds == null) {
                                    Log.d(TAG, "no relevant recipes in the database");
                                    getPescRecipes(EASY_LEVEL);
                                } else {
                                    //If there are relevant recipes, then fetch them from the database and
                                    //update the adapters for the recycler view
                                    Log.d(TAG, "found some relevant recipes in the database");
                                    for (Integer id : recipeIds) {
                                        if (noDuplicateRecipes(id, recipes)) {
                                            recipes.add(recipesDao.findById(id));
                                        }
                                    }
                                    setAdapters();
                                }
                            }
                        });
                        break;
                    case "MED":
                        break;
                    case "HARD":
                        break;
                }

        }


        //Get recipes from database to display in recycler view
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "Number of recipes in database: " + recipesDao.getAll().size());
//                if (recipesDao.getAll().size() > 0) {
//                    switch(recipeType) {
//                        case "VEG":
//                            recipes = recipesDao.findVegRecipes(EASY_LEVEL);
//                            break;
//                        case "GLUTENF":
//                            recipes = recipesDao.findGlutenFRecipes(EASY_LEVEL);
//                            break;
//                    }
//                    setAdapters();
//                }
//            }
//        });
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
        getRecipes(null, null, "vegetarian", null, level);
    }
    private void getPescRecipes(int level) {
        getRecipes(null, null, "pescetarian", null, level);
    }



    //Gets recipes from API - Preparing the recipes for the RecyclerView
    private void getRecipes(String query, String cuisine, String diet, String intolerances, int maxReadyTime) {
        Call<RecipeSearchResult> call = RetrofitClient.getInstance().getMyApi()
                //change maxReadyTime to adjust difficulty level
                .recipeResults(query, cuisine, diet, intolerances, true, maxReadyTime, "time", SpoonacularClient.apiKey);

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
                addRecipeToDb(recipeInfo);
            }

            @Override
            public void onFailure(Call<RecipeInformationResult> call, Throwable t) {
                Log.i(TAG, "Failed API Call" + t.getCause());

            }
        });
    }

    private void addRecipeToDb(RecipeInformationResult result) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            //Add recipe to the recipes database
            recipesDao.insertAll(result);
            //Add recipeId, type and level in the recipeType table
            String recipeTypeId = recipeType + result.getId() + difficultyLevel;
            recipeTypeDao.insertAll(new RecipeType(recipeTypeId, recipeType, result.getId(), difficultyLevel));

            Log.d(TAG, "Added to database: " + result.getTitle());
            recipes.removeAll(recipes);
            recipes = recipesDao.getAll();
            //dont htink i need this??
            setAdapters();
        });
    }

    private void setAdapters() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "setting adapter");
                adapter.updateRecipeList(recipes);
            }
        });
    }

    public static boolean noDuplicateRecipes(Integer id, List<RecipeInformationResult> list) {
        for (RecipeInformationResult r : list) {
            if (r.getId().equals(id)) {
                Log.d(TAG, "Trying to add an existing recipe!");
                return false;
            }
        }
        return true;
    }

}