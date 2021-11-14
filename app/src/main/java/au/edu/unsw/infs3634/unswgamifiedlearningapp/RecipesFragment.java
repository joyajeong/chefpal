package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
    private static final String TAG = "RecipesFragment";
//    private List<RecipeInformationResult> recipeList = RecipeLevelsActivity.recipeResults;
                                            //could just have easy, med & hard if i could call api when
                                            //button is clicked
//    private List<RecipeInformationResult> vegEasy = RecipeLevelsActivity.vegEasy;
//    private List<RecipeInformationResult> vegMed = RecipeLevelsActivity.vegMed;
//    private List<RecipeInformationResult> vegHard = RecipeLevelsActivity.vegHard;
    private int userPoints;
    public static int EASY_LIMIT = 1000;
    public static int MED_LIMIT = 3000;
    public static int HARD_LIMIT = 5000;
    RecipeSearchResult searchResult;
    TextView recipeTitle;
    RecipeSearchResult recipes;
    Button btnVeg, btnPesc;


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
                launchRecipeLevels("VEG");
            }
        });

        btnPesc = getView().findViewById(R.id.btnPesc);
        btnPesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecipeLevels("PESC");
            }
        });
    }

    private void launchRecipeLevels(String recipeType) {
        Intent intent = new Intent(getActivity(), RecipeLevelsActivity.class);
        intent.putExtra("RECIPE_TYPE", recipeType);
        startActivity(intent);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

}