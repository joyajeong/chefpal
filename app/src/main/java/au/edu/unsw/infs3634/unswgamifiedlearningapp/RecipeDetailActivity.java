package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecipeDetailActivity extends AppCompatActivity {

    private static String TAG = "RecipeDetailActivity";
    private Integer selectedRecipeId;
    private RecipeInformationResult selectedRecipe;
    private TextView tvRecipeName, tvRecipeTime, tvSourceURL, tvServings, tvIngredients;
    private LinearLayout layout;
    private ImageButton btnFavourite;
    private String currUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Bundle bundle = getIntent().getExtras();
        selectedRecipeId = Integer.parseInt(bundle.getString("RECIPE_ID"));
        Log.d(TAG, "Selected recipe id: " + selectedRecipeId);

        //Recipes database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "recipes").fallbackToDestructiveMigration().build();
        RecipesDao recipesDao = db.recipesDao();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                selectedRecipe = recipesDao.findById(selectedRecipeId);
                setRecipeInfo();
            }
        });

        layout = findViewById(R.id.ingredient_checkboxes_layout);
        tvRecipeName  = findViewById(R.id.tvRecipeName);
        tvRecipeTime = findViewById(R.id.tvRecipeTime);
        tvSourceURL = findViewById(R.id.tvSourceURL);
        tvServings = findViewById(R.id.tvServings);
        tvIngredients = findViewById(R.id.tvIngredients);
        btnFavourite = findViewById(R.id.btnFavourite);

        currUserID = MainActivity.currUserID;

        //UserFavouriteRecipe Database
        AppDatabase db1 = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "userFavouriteRecipe").fallbackToDestructiveMigration().build();
        UserFavouriteRecipeDao userFavouriteRecipeDao = db1.userFavouriteRecipeDao();

        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    String id = currUserID + selectedRecipeId;
                    //If the user has not already liked the recipe, add it
                    if (userFavouriteRecipeDao.findById(id) == null) {
                        userFavouriteRecipeDao.insertAll(new UserFavouriteRecipe(id, currUserID, selectedRecipeId, ""));
                        Log.d(TAG, "Favourited a recipe: " +  id);
                    } else {
                        Log.d(TAG, "Already liked the recipe!");
                    }
                });

            }
        });
    }

    private void setRecipeInfo() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvRecipeName.setText(selectedRecipe.getTitle());
                tvRecipeTime.setText("Ready in: " + selectedRecipe.getReadyInMinutes());
                tvSourceURL.setText(selectedRecipe.getSourceUrl());
                tvServings.setText(String.valueOf(selectedRecipe.getServings()));
                tvIngredients.setText(formatIngredients());            }
        });
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
        }

        String formattedIngredients = Arrays.toString(ingredients)
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .replace(",", "\n") //separate each line
                .trim(); //remove trailing spaces from partially initialized arrays
        return formattedIngredients;
    }

}