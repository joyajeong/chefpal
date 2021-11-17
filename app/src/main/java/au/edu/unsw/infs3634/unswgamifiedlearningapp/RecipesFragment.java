package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RecipesFragment extends Fragment {

    private static final String TAG = "RecipesFragment";
    TextView recipeTitle;

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Recipes");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recipeTitle = getView().findViewById(R.id.tvRecipeTitle);

        //When user clicks Pescatarian
        CardView cvPesc = (CardView) getView().findViewById(R.id.cardViewPesc);
        cvPesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRecipeLevels("PESC");
            }
        });

        //When user clicks Vegetarian
        CardView cvVeg = (CardView) getView().findViewById(R.id.cardViewVeg);
        cvVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRecipeLevels("VEG");
            }
        });

        //When user clicks Italian
        CardView cvItaly = (CardView) getView().findViewById(R.id.cardViewIta);
        cvItaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRecipeLevels("ITA");
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