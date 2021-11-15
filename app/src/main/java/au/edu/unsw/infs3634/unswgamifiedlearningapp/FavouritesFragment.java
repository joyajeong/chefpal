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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavouritesFragment extends Fragment {

    private static String TAG = "FavouritesFragment";
    private String currUserID;
    private TextView tvFavouriteRecipes;
    private List<UserFavouriteRecipe> favUserRecipes;
    private List<Integer> favRecipeIds;
    private RecyclerView recyclerView;
    private RecipeRecyclerViewAdapter adapter;
    private List<RecipeInformationResult> favRecipes = new ArrayList<>();
    private List<RecipeInformationResult> initalRecipes = new ArrayList<>();
    private RecipeInformationResult recipeInfo;
    private RecipesDao recipesDao;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites3, container, false);
    }

    //Do any logic here
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Set title of Action Bar and remove shadow
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Your Favourite Recipes");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(0);

        tvFavouriteRecipes = getView().findViewById(R.id.tvFavouriteRecipes);
        currUserID = MainActivity.currUserID;

        //Recipes database
        AppDatabase db = Room.databaseBuilder(getActivity(),
                AppDatabase.class, "recipes").fallbackToDestructiveMigration().build();
        recipesDao = db.recipesDao();

        //UserFavouriteRecipe Database
        AppDatabase db1 = Room.databaseBuilder(getActivity(),
                AppDatabase.class, "userFavouriteRecipe").fallbackToDestructiveMigration().build();

        UserFavouriteRecipeDao userFavouriteRecipeDao = db1.userFavouriteRecipeDao();

        //Checking how many recipes they have liked
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            favRecipeIds = userFavouriteRecipeDao.findFavRecipeIdByUserId(currUserID);
//            favUserRecipes = userFavouriteRecipeDao.findFavRecipesByUserId(currUserID);
//            Log.d(TAG, "Current favourite recipes: " + favUserRecipes.size());
//            setText(tvFavouriteRecipes, "Number of favourite recipes: " + favUserRecipes.size());
            getRecipes(favRecipeIds);
        });

        recyclerView = getView().findViewById(R.id.rvFavRecipes);

        try {
            setRecyclerView();
        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e));
        }

    }

    private void setText(final TextView text,final String value){
        getActivity().runOnUiThread(new Runnable() {
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
                Intent intent = new Intent(getActivity(), FavouriteRecipeDetailActivity.class);
                intent.putExtra("RECIPE_ID", id);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        };
        //Created an adapter and supply the song data to be displayed
        adapter = new RecipeRecyclerViewAdapter(initalRecipes, listener);
        recyclerView.setAdapter(adapter);

        //Set linear layout of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getRecipes(List<Integer> recipeIds){
        for (Integer id : recipeIds) {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                addToFavRecipes(recipesDao.findById(id));
            });
        }
    }

    private void addToFavRecipes(RecipeInformationResult r) {
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
                Log.d(TAG, "Number of fav recipes: " + favRecipes.size());
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