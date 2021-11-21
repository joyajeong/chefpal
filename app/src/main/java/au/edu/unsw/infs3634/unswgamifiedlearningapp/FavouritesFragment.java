package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.RecipesDao;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.UserFavouriteRecipeDao;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.RecipeAPIEntities.RecipeInformationResult;


public class FavouritesFragment extends Fragment {

    private static String TAG = "FavouritesFragment";
    private String currUserID;
    private List<UserFavouriteRecipe> favUserRecipes;
    private List<Integer> favRecipeIds;
    private RecyclerView recyclerView;
    private RecipeRecyclerViewAdapter adapter;
    private List<RecipeInformationResult> favRecipes = new ArrayList<>();
    private List<RecipeInformationResult> initalRecipes = new ArrayList<>();
    private RecipeInformationResult recipeInfo;
    private RecipesDao recipesDao;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites3, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "in on start");
        //UserFavouriteRecipe Database
        AppDatabase db1 = Room.databaseBuilder(getActivity(),
                AppDatabase.class, "userFavouriteRecipe").fallbackToDestructiveMigration().build();

        UserFavouriteRecipeDao userFavouriteRecipeDao = db1.userFavouriteRecipeDao();

        //Checking how many recipes they have favourited
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            //Gets a list of favourite recipe IDs
            favRecipeIds = userFavouriteRecipeDao.findFavRecipeIdByUserId(currUserID);
            Log.d(TAG, "Current favourite recipes: " + favRecipeIds.size());
            //If user has 0 favourited recipes, update recycler view with empty list
            if (favRecipeIds.size() == 0) {
                updateRecyclerView(initalRecipes);
            }
            //If user has favourited recipes, remove the current list and get recipes
            favRecipes.removeAll(favRecipes);
            getRecipes(favRecipeIds);
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Set title of Action Bar and remove shadow
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Your Favourite Recipes");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(0);

        //Get current user ID
        currUserID = MainActivity.currUserID;

        //Recipes database
        AppDatabase db = Room.databaseBuilder(getActivity(),
                AppDatabase.class, "recipes").fallbackToDestructiveMigration().build();
        recipesDao = db.recipesDao();

        //Initialise recycler view
        recyclerView = getView().findViewById(R.id.rvFavRecipes);

        try {
            setRecyclerView();
        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e));
        }

    }

    private void setRecyclerView() {
        Log.d(TAG, "in setRecyclerView");
        RecipeRecyclerViewAdapter.ClickListener listener = new RecipeRecyclerViewAdapter.ClickListener() {
            @Override
            public void onRecipeClick(View view, String id) {
                //When recipe is clicked, go to a detailed view of the recipe
                Log.d(TAG, "recipe " + id + " clicked");
                Intent intent = new Intent(getActivity(), FavouriteRecipeDetailActivity.class);
                intent.putExtra("RECIPE_ID", id);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        };
        //Created an adapter and supply the recipes to be displayed
        adapter = new RecipeRecyclerViewAdapter(initalRecipes, listener);
        recyclerView.setAdapter(adapter);

        //Set linear layout of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getRecipes(List<Integer> recipeIds){
        //For every recipe ID, get the recipe from the Recipe database
        for (Integer id : recipeIds) {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                addToFavRecipes(recipesDao.findById(id));
            });
        }
    }

    private void addToFavRecipes(RecipeInformationResult r) {
        //Check if there are duplicates and if there's no duplicates, add it to the list to be used
        //by the recycler view
        if (RecipeLevelsListActivity.noDuplicateRecipes(r.getId(), favRecipes) && r != null) {
            Log.d(TAG, "Recipe from API: " + r.getTitle());
            favRecipes.add(r);
        }
        updateRecyclerView(favRecipes);
    }

    private void updateRecyclerView(List<RecipeInformationResult> recipes) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Number of fav recipes: " + recipes.size());
                adapter.updateRecipeList(recipes);
            }
        });

    }

    //Instantiate menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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