package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecipeDetailActivity extends AppCompatActivity {

    private static String TAG = "RecipeDetailActivity";
    private Integer selectedRecipeId;
    private RecipeInformationResult selectedRecipe;
    private TextView tvRecipeName, tvRecipeTime, tvServings, tvScore;
    private LinearLayout layout;
    private ImageButton btnFavourite;
    private String currUserID;
    private ImageView ivDetailPic;
    private Button btnMethod;
    private ImageButton btnBack, btnExtraSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        getSupportActionBar().hide();

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
        tvServings = findViewById(R.id.tvServings);
        btnFavourite = findViewById(R.id.btnFavourite);
        ivDetailPic = findViewById(R.id.ivDetailPic);
        btnMethod = findViewById(R.id.btnMethod);
        btnBack = findViewById(R.id.btnBack2);
        tvScore = findViewById(R.id.tvScoreDetail);
        btnExtraSearch = findViewById(R.id.btnExtraSearch);

        currUserID = MainActivity.currUserID;

        //UserFavouriteRecipe Database
        AppDatabase db1 = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "userFavouriteRecipe").fallbackToDestructiveMigration().build();
        UserFavouriteRecipeDao userFavouriteRecipeDao = db1.userFavouriteRecipeDao();

        //Checking if they have already favourited this recipe
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            //The selected recipeid is in the array of their favourited recipe ids
            if (userFavouriteRecipeDao.findFavRecipeIdByUserId(currUserID).contains(selectedRecipeId)) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnFavourite.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    }
                });
            }
        });

        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = currUserID + selectedRecipeId;
                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    //If the user has not already liked the recipe, add it
                    if (userFavouriteRecipeDao.findById(id) == null) {
                        userFavouriteRecipeDao.insertAll(new UserFavouriteRecipe(id, currUserID, selectedRecipeId, ""));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnFavourite.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                            }
                        });
                        showToast("Favourited the recipe");
                        Log.d(TAG, "Favourited a recipe: " +  id);
                    } else {
                        //TODO: delete the recipe from fav if they have already liked it
                        UserFavouriteRecipe toDelete = userFavouriteRecipeDao.findById(id);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnFavourite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                            }
                        });
                        userFavouriteRecipeDao.delete(toDelete);
                        showToast("Removed recipe from favourites");
                    }
                });

            }
        });

        btnMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(selectedRecipe.getSourceUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnExtraSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "How to cook " + selectedRecipe.getTitle());
                startActivity(intent);
            }
        });
    }

    private void setRecipeInfo() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(8));
                Glide.with(RecipeDetailActivity.this)
                        .load(selectedRecipe.getImage())
                        .apply(requestOptions)
                        .into(ivDetailPic);
                tvRecipeName.setText(selectedRecipe.getTitle());
                tvRecipeTime.setText(selectedRecipe.getReadyInMinutes() + " minutes");
                tvServings.setText(String.valueOf(selectedRecipe.getServings()));
                tvScore.setText(String.valueOf(selectedRecipe.getSpoonacularScore()));
                formatIngredients();
            }
        });
    }

    //TODO find a better way to format ingredients/move it to recipeinformationresult??
    private void formatIngredients() {
        int lengthOfIngredients = selectedRecipe.getExtendedIngredients().size();
        List<ExtendedIngredient> ingredientList = selectedRecipe.getExtendedIngredients();

        String ingredients[] = new String[lengthOfIngredients];

        for (int i = 0; i < lengthOfIngredients; i++) {
            ingredients[i] = ingredientList.get(i).getOriginal();
            CheckBox cb = new CheckBox(this);
            cb.setText(ingredients[i]);
            layout.addView(cb);
        }
    }

    private void showToast(String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                toast.show();            }
        });

    }
}