package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private static String TAG = "RecipeDetailActivity";
    private Integer selectedRecipeId;
    private RecipeInformationResult selectedRecipe;
    private TextView tvRecipeName, tvRecipeTime, tvSourceURL, tvServings, tvIngredients;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Bundle bundle = getIntent().getExtras();
        selectedRecipeId = Integer.parseInt(bundle.getString("RECIPE_ID"));
        Log.d(TAG, "Selected recipe id: " + selectedRecipeId);

        try {
            selectedRecipe = RecipeInformationResult.getRecipeById(selectedRecipeId);
        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e));
        }

        layout = findViewById(R.id.ingredient_checkboxes_layout);

        tvRecipeName  = findViewById(R.id.tvRecipeName);
        tvRecipeTime = findViewById(R.id.tvRecipeTime);
        tvSourceURL = findViewById(R.id.tvSourceURL);
        tvServings = findViewById(R.id.tvServings);
        tvIngredients = findViewById(R.id.tvIngredients);

        setRecipeInfo();
    }

    private void setRecipeInfo() {
        tvRecipeName.setText(selectedRecipe.getTitle());
        tvRecipeTime.setText("Ready in: " + selectedRecipe.getReadyInMinutes());
        tvSourceURL.setText(selectedRecipe.getSourceUrl());
        tvServings.setText(String.valueOf(selectedRecipe.getServings()));
        tvIngredients.setText(formatIngredients());
    }

    //TODO find a better way to format ingredients/move it to recipeinformationresult??
    private String formatIngredients() {
        int lengthOfIngredients = selectedRecipe.getExtendedIngredients().size();
        List<ExtendedIngredient> ingredientList = selectedRecipe.getExtendedIngredients();

        String ingredients[] = new String[lengthOfIngredients];

        for (int i = 0; i < lengthOfIngredients; i++) {
            ingredients[i] = ingredientList.get(i).getOriginal();
            CheckBox cb = new CheckBox(this);
            cb.setText(ingredients[i]);
            layout.addView(cb);
            Log.d(TAG, "Ingredient: " + ingredientList.get(i).getOriginal());
        }



        String formattedIngredients = Arrays.toString(ingredients)
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .replace(",", "\n") //separate each line
                .trim(); //remove trailing spaces from partially initialized arrays
        return formattedIngredients;
    }

}