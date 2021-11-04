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
    public static List<RecipeInformationResult> recipeEasy = new ArrayList<>();
    public static List<RecipeInformationResult> recipeMed = new ArrayList<>();
    //maybe move these to recipes fragment
    public static List<RecipeInformationResult> vegEasy = new ArrayList<>();
    public static List<RecipeInformationResult> vegMed = new ArrayList<>();
    public static List<RecipeInformationResult> vegHard = new ArrayList<>();
    public static List<RecipeInformationResult> itEasy = new ArrayList<>();
    public static List<RecipeInformationResult> itMed = new ArrayList<>();
    public static List<RecipeInformationResult> itHard = new ArrayList<>();
    private String recipeType;
    private RecyclerView recyclerViewEasy, recyclerViewMed;
    private RecipeRecyclerViewAdapter adapter;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciple_levels);
//        getRecipes();
        Bundle bundle = getIntent().getExtras();
        recipeType = bundle.getString("RECIPE_TYPE");
        Log.d(TAG, recipeType);

        switch(recipeType) {
            case "VEG":
                recipeEasy = vegEasy;
                recipeMed = vegMed;
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get the RecyclerView and implement ClickListener
        recyclerViewEasy = findViewById(R.id.rvRecipesEasy);
        recyclerViewMed = findViewById(R.id.rvRecipesMed);

        RecipeRecyclerViewAdapter.ClickListener listener = new RecipeRecyclerViewAdapter.ClickListener() {
            @Override
            public void onRecipeClick(View view, String id) {
                Log.d(TAG, "recipe " + id + "clicked");
            }
        };

        if (recipeMed == null || recipeMed.size() == 0) {
            setRecyclerView(recipeEasy, recyclerViewEasy);
        }
//        setRecyclerView(recipeEasy, recyclerViewEasy);
//        setRecyclerView(recipeMed, recyclerViewMed);


//        //Created an adapter and supply the song data to be displayed
//        adapter = new RecipeRecyclerViewAdapter(recipeResults, listener);
//        recyclerViewEasy.setAdapter(adapter);
//
//        //Set linear layout of RecyclerView
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerViewEasy.setLayoutManager(layoutManager);
    }

    private void setRecyclerView(List<RecipeInformationResult> recipe, RecyclerView recyclerView) {
        RecipeRecyclerViewAdapter.ClickListener listener = new RecipeRecyclerViewAdapter.ClickListener() {
            @Override
            public void onRecipeClick(View view, String id) {
                Log.d(TAG, "recipe " + id + " clicked");
            }
        };
        //Created an adapter and supply the song data to be displayed
        adapter = new RecipeRecyclerViewAdapter(recipe, listener);
        recyclerView.setAdapter(adapter);

        //Set linear layout of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

}