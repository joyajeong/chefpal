package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.RecipeTypeDao;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.RecipesDao;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.RecipeAPIEntities.RecipeInformationResult;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.RecipeAPIEntities.RecipeSearchResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeLevelsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static RecipeRecyclerViewAdapter adapter;
    private static final String TAG = "RecipeLevelsListActivity";
    private List<RecipeInformationResult> recipes = new ArrayList<>();
    private List<RecipeInformationResult> initialRecipes = new ArrayList<>();
    private RecipesDao recipesDao;
    private RecipeTypeDao recipeTypeDao;
    private String difficultyLevel, recipeType;
    public static int NUM_RESULTS = 5;
    private int level;
    private RecipeSearchResult searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_levels_list);

        //Initalise the recycler view
        recyclerView = findViewById(R.id.rvRecipes);

        //Receive the recipe type and difficulty level chosen by the user
        Bundle bundle = getIntent().getExtras();
        difficultyLevel = bundle.getString("DIFFICULTY_LEVEL");
        recipeType = bundle.getString("RECIPE_TYPE");
        Log.d(TAG, difficultyLevel);
        Log.d(TAG, recipeType);
        setRecyclerView();
        recipes.removeAll(recipes);

        //Check which level the user clicked on and set the Heading and level (recipe time limit)
        switch(difficultyLevel) {
            case "EASY":
                level = 15;
                setActionBarTitle("Easy");
                break;
            case "MED":
                level = 30;
                setActionBarTitle("Medium");
                break;
            case "HARD":
                level = 45;
                setActionBarTitle("Hard");
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

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //Gets the relevant recipe IDs (by recipe type and difficulty level)
                List<Integer> recipeIds = recipeTypeDao.getAllRecipeIdByTypeLevel(recipeType, difficultyLevel);
                Log.d(TAG, "number of relevant recipes: " + recipeIds.size());
                //If there are no relevant recipes in the database currently, then get the recipes
                //using the SpoonacularApi
                if (recipeIds.size() == 0 || recipeIds == null) {
                    Log.d(TAG, "no relevant recipes in the database");
                    getRecipesFromApi(recipeType);
                } else {
                    //If there are relevant recipes, then fetch them from the database and
                    //update the adapters for the recycler view
                    Log.d(TAG, "found some relevant recipes in the database");
                    for (Integer id : recipeIds) {
                        if (noDuplicateRecipes(id, recipes)) {
                            recipes.add(recipesDao.findById(id));
                            Log.d(TAG, "Name " + recipesDao.findById(id).getTitle() +" spoonacular score" + recipesDao.findById(id).getSpoonacularScore());
                        }
                    }
                    setAdapters();
                }
            }
        });

        }

    @Override
    protected void onStart() {
        super.onStart();
        //Get the RecyclerView and implement ClickListener
        setRecyclerView();
    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setElevation(0);
    }

    private void setRecyclerView() {
        Log.d(TAG, "in setRecyclerView");
        RecipeRecyclerViewAdapter.ClickListener listener = new RecipeRecyclerViewAdapter.ClickListener() {
            @Override
            public void onRecipeClick(View view, String id) {
                //When item in recycler view is clicked, go to a detailed view of the recipe
                Log.d(TAG, "recipe " + id + " clicked");
                Intent intent = new Intent(RecipeLevelsListActivity.this, RecipeDetailActivity.class);
                intent.putExtra("RECIPE_ID", id);
                startActivity(intent);
            }
        };
        //Created an adapter and supply the recipe data to be displayed
        adapter = new RecipeRecyclerViewAdapter(initialRecipes, listener);
        recyclerView.setAdapter(adapter);

        //Set linear layout of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    //Check the type of recipe chosen
    private void getRecipesFromApi(String type) {
        switch(type) {
            case "VEG":
                getVegRecipes(level);
                break;
            case "PESC":
                getPescRecipes(level);
                break;
            case "ITA":
                getItalianRecipes(level);
                break;
        }
    }

    private void getVegRecipes(int level) {
        getRecipes(null, null, "vegetarian", null, level);
    }
    private void getPescRecipes(int level) {
        getRecipes(null, null, "pescetarian", null, level);
    }
    private void getItalianRecipes(int level) {
        getRecipes(null, "italian", null, null, level);
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
                //If API response is successful, get details of the recipe
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

    //Gets more details on the recipe
    private void getRecipeInfo(int id) {
        RecipeInformationResult recipeInfo;
        Call<RecipeInformationResult> call = RetrofitClient.getInstance().getMyApi()
                .recipeInformationResults(id, false, SpoonacularClient.apiKey);

        call.enqueue(new Callback<RecipeInformationResult>() {
            @Override
            public void onResponse(Call<RecipeInformationResult> call, Response<RecipeInformationResult> response) {
                RecipeInformationResult recipeInfo = response.body();
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
            Log.d(TAG, "Recipe Type: " + recipeType);

            Log.d(TAG, "Added to database: " + result.getTitle());
            recipes.add(result);
            //Update the recycler view
            setAdapters();
        });
    }

    private void setAdapters() {
        Log.d(TAG, "number of recipes: " + recipes.size());
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "setting adapter");
                adapter.updateRecipeList(recipes);
            }
        });
    }

    //Check if recipe already exists in the list to be used by the recycler view
    public static boolean noDuplicateRecipes(Integer id, List<RecipeInformationResult> list) {
        for (RecipeInformationResult r : list) {
            if (r.getId().equals(id)) {
                Log.d(TAG, "Trying to add an existing recipe!");
                return false;
            }
        }
        return true;
    }

    //Instantiate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recipes_list, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setQueryHint("Search for recipes");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    //Reacts to when user interacts with the sort menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.sortRecipeName:
                //Sort by recipe name
                adapter.sort(1);
                return true;
            case R.id.sortPopularity:
                //Sort by recipe popularity
                adapter.sort(2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}