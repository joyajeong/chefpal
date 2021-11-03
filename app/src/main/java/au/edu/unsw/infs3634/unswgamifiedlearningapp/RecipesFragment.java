package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int recipeId;
    private static final String TAG = "RecipeFragment";
    TextView recipeTitle;
    RecipeSearchResult recipes;
    Button btnVeg;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipesFragment newInstance(String param1, String param2) {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }

    //Do any logic here
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recipeTitle = getView().findViewById(R.id.tvRecipeTitle);
        btnVeg = getView().findViewById(R.id.btnVeg);
        btnVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecipeLevelsActivity.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });
        //Test out Spoonacular API
        getRecipes();
    }

    //Gets recipes from API - just a test
    private void getRecipes() {
        Call<RecipeSearchResult> call = RetrofitClient.getInstance().getMyApi()
                .recipeResults(null, null, null, true, 20, SpoonacularClient.apiKey);

        call.enqueue(new Callback<RecipeSearchResult>() {
            @Override
            public void onResponse(Call<RecipeSearchResult> call, Response<RecipeSearchResult> response) {
                recipes = response.body();

                if (response.body() != null) {
                    recipeId = recipes.getResults().get(0).getId();
                    getRecipeInfo(recipeId);
                    recipeTitle.setText(recipes.getResults().get(0).getTitle());
                    Log.d(TAG, "Recipe Title: " + recipes.getResults().get(0).getTitle());
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
        Call<RecipeInformationResult> call = RetrofitClient.getInstance().getMyApi()
                .recipeInformationResults(id, SpoonacularClient.apiKey);

        call.enqueue(new Callback<RecipeInformationResult>() {
            @Override
            public void onResponse(Call<RecipeInformationResult> call, Response<RecipeInformationResult> response) {
                RecipeInformationResult recipeInfo = response.body();
                Log.i(TAG, recipeInfo.getSourceUrl());
            }

            @Override
            public void onFailure(Call<RecipeInformationResult> call, Throwable t) {
                Log.i(TAG, "Failed API Call" + t.getCause());

            }
        });
    }

}