package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteActivity extends AppCompatActivity {
    //Maybe change back to FavouriteFragment?

    private static String TAG = "Favorite Activity";
    private String currUserID;
    private TextView tvFavouriteRecipes;
    private List<UserFavouriteRecipe> favUserRecipes;
    private RecyclerView recyclerView;
    private RecipeRecyclerViewAdapter adapter;
    private List<RecipeInformationResult> favRecipes = new ArrayList<>();
    private List<RecipeInformationResult> initalRecipes = new ArrayList<>();
    private RecipeInformationResult recipeInfo;


//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;


//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FavouritesFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static AccountFragment newInstance(String param1, String param2) {
//        AccountFragment fragment = new AccountFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);


//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.activity_favorite, container, false);
////    }
//
//    //Do any logic here
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvFavouriteRecipes = findViewById(R.id.tvFavouriteRecipes);

        //UserFavouriteRecipe Database
        AppDatabase db = Room.databaseBuilder(FavoriteActivity.this,
                AppDatabase.class, "userFavouriteRecipe").fallbackToDestructiveMigration().build();

        currUserID = MainActivity.currUserID;
        UserFavouriteRecipeDao userFavouriteRecipeDao = db.userFavouriteRecipeDao();

        //Checking how many recipes they have liked
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            favUserRecipes = userFavouriteRecipeDao.findFavRecipesByUserId(currUserID);
            Log.d(TAG, "Current favourite recipes: " + favUserRecipes.size());
            setText(tvFavouriteRecipes, "Number of favourite recipes: " + favUserRecipes.size());
            getRecipes(favUserRecipes);
        });

        recyclerView = findViewById(R.id.rvFavRecipes);

        try {
            setRecyclerView();
        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e));
        }


    }

    private void setText(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    private void setRecyclerView() {
        Log.d(TAG, "in setRecyclerView");
        RecipeRecyclerViewAdapter.ClickListener listener = new RecipeRecyclerViewAdapter.ClickListener() {
            @Override
            public void onRecipeClick(View view, String id) {
                Log.d(TAG, "recipe " + id + " clicked");
                Intent intent = new Intent(FavoriteActivity.this, FavouriteRecipeDetailActivity.class);
                startActivity(intent);
                //Favorite.this.overridePendingTransition(0, 0);
            }
        };
        //Created an adapter and supply the song data to be displayed
        adapter = new RecipeRecyclerViewAdapter(initalRecipes, listener);
        recyclerView.setAdapter(adapter);

        //Set linear layout of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getRecipes(List<UserFavouriteRecipe> favUserRecipes){
        for (UserFavouriteRecipe recipe : favUserRecipes) {
            getRecipeInfo(recipe.getRecipeId());
        }
    }

    private RecipeInformationResult getRecipeInfo(int id) {
        Call<RecipeInformationResult> call = RetrofitClient.getInstance().getMyApi()
                .recipeInformationResults(id, false, SpoonacularClient.apiKey);

        call.enqueue(new Callback<RecipeInformationResult>() {
            @Override
            public void onResponse(Call<RecipeInformationResult> call, Response<RecipeInformationResult> response) {
                recipeInfo = response.body();
                //check if response body is null
                Log.d(TAG, "Source URL: " + recipeInfo.getSourceUrl());
                Log.d(TAG, "Recipe Title: " + recipeInfo.getTitle());
                Log.d(TAG, "Recipe time: " + recipeInfo.getReadyInMinutes());
                addToFavRecipes(recipeInfo);
            }

            @Override
            public void onFailure(Call<RecipeInformationResult> call, Throwable t) {
                Log.i(TAG, "Failed API Call" + t.getCause());

            }
        });
        return recipeInfo;
    }

    private void addToFavRecipes(RecipeInformationResult r) {
        if (RecipeLevelsListActivity.noDuplicateRecipes(r.getId(), favRecipes) && r != null) {
            Log.d(TAG, "Recipe from API: " + r.getTitle());
            favRecipes.add(r);
        }
        updateRecyclerView(favRecipes);
    }

    private void updateRecyclerView(List<RecipeInformationResult> recipes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Number of fav recipes: " + favRecipes.size());
                adapter.updateRecipeList(recipes);
            }
        });

    }

}
