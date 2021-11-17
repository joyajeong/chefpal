package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavouriteRecipeDetailActivity extends AppCompatActivity {

    private static String TAG = "FavouriteRecipeDetailActivity";
    private EditText etNotes;
    private Button btnNotes, btnComplete, btnMethod;
    private ImageButton btnBack, btnFavourite, btnExtraSearch;
    private RecipeInformationResult selectedRecipe;
    private Integer selectedRecipeId;
    private UserFavouriteRecipe userFavRecipe;
    private UserRecipeCompletedDao userRecipeCompletedDao;
    private UserDao userDao;
    private String notes, id;
    private boolean completed = false;
    private TextView tvRecipeName, tvRecipeTime, tvServings, tvScore;
    private LinearLayout layout;
    private String currUserID;
    private ImageView ivDetailPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_recipe_detail);
        //Hides action bar
        getSupportActionBar().hide();

        //Gets the selected recipe ID from the recycler view
        Bundle bundle = getIntent().getExtras();
        selectedRecipeId = Integer.parseInt(bundle.getString("RECIPE_ID"));
        Log.d(TAG, "Selected recipe id: " + selectedRecipeId);
        //The id for UserFavouriteRecipe class
        id = MainActivity.currUserID + selectedRecipeId;

        //Initalising the UI elements
        layout = findViewById(R.id.ingredient_checkboxes_layout);
        tvRecipeName  = findViewById(R.id.tvRecipeName);
        tvRecipeTime = findViewById(R.id.tvRecipeTime);
        tvServings = findViewById(R.id.tvServings);
        btnFavourite = findViewById(R.id.btnFavourite);
        ivDetailPic = findViewById(R.id.ivDetailPic);
        btnMethod = findViewById(R.id.btnMethod);
        btnBack = findViewById(R.id.btnBack);
        tvScore = findViewById(R.id.tvScoreDetail);
        btnExtraSearch = findViewById(R.id.btnExtraSearch);
        btnFavourite.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
        etNotes = findViewById(R.id.etNotes);

        //Get the text from the notes textbox
        Editable newNotes = etNotes.getText();
        Log.d(TAG, "new note: " + newNotes);

        //When user clicks on the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Recipes database
        AppDatabase dbR = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "recipes").fallbackToDestructiveMigration().build();
        RecipesDao recipesDao = dbR.recipesDao();
        //Find recipe using ID
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                selectedRecipe = recipesDao.findById(selectedRecipeId);
                //Set the UI elements to the recipe information
                setRecipeInfo();
            }
        });

        //User database
        AppDatabase dbU = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user").fallbackToDestructiveMigration().build();
        userDao = dbU.userDao();

        //UserFavouriteRecipe Database
        AppDatabase db = Room.databaseBuilder(this,
                AppDatabase.class, "userFavouriteRecipe").fallbackToDestructiveMigration().build();
        UserFavouriteRecipeDao userFavouriteRecipeDao = db.userFavouriteRecipeDao();

        //Getting the notes of the selected recipe
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            userFavRecipe = userFavouriteRecipeDao.findById(id);
            Log.d(TAG, "Current selected recipe: " + userFavRecipe.getId());
            notes = userFavouriteRecipeDao.findById(id).getNotes();
            Log.d(TAG, "notes: " + notes);
            setNotes(String.valueOf(notes));
        });

        //When the favourite button is clicked - this is in the favourites page and therefore clicking
        //it will remove the recipe from the favourites list
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    //Delete recipe from favourites
                    UserFavouriteRecipe toDelete = userFavouriteRecipeDao.findById(id);
                    userFavouriteRecipeDao.delete(toDelete);
                    showToast("Removed recipe from Favourites");

                    //Change the favourite button to hollow
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnFavourite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                        }
                    });
                });
                //Go back to the Favourites list
                onBackPressed();
            }
        });

        //When the methods button is clicked, it links the user to the recipe source with the steps
        btnMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(selectedRecipe.getSourceUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        //When the save notes button is clicked, it saves the updated notes in the database
        btnNotes = findViewById(R.id.btnNotes);
        btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable newNotes = etNotes.getText();
                Log.d(TAG, "new note: " + newNotes);
                //Setting the new note
                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    userFavouriteRecipeDao.updateNotes(String.valueOf(newNotes), id);
                    showToast("Note saved");
                });
            }
        });

        //When the search button is clicked, it triggers a Google search to find more information on the recipe
        btnExtraSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "How to cook " + selectedRecipe.getTitle());
                startActivity(intent);
            }
        });

        //UserCompletedRecipe Database
        AppDatabase dbURC = Room.databaseBuilder(this,
                AppDatabase.class, "userRecipeCompleted").fallbackToDestructiveMigration().build();
        userRecipeCompletedDao = dbURC.userRecipeCompletedDao();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //Checking whether the user has completed the recipe
                List<UserRecipeCompleted> completedRecipes = userRecipeCompletedDao.completedRecipesByUser(MainActivity.currUserID, selectedRecipeId);
                //If the user has not completed it
                if (completedRecipes.size() == 0) {
                    Log.d(TAG, "User has not completed the recipe already");
                } else {
                    //If the user has completed the recipe
                    completed = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Disable the "Recipe Completed" button
                            btnComplete.setEnabled(false);
                            btnComplete.setText("You have already completed this recipe");
                        }
                    });
                    Log.d(TAG, "user has completed the recipe already");
                }
            }
        });

        //RecipeType Database
        AppDatabase dbRT = Room.databaseBuilder(this,
                AppDatabase.class, "recipeType").fallbackToDestructiveMigration().build();
        RecipeTypeDao recipeTypeDao = dbRT.recipeTypeDao();

        //When the "Recipe Completed" button is clicked
        btnComplete = findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        //Getting the difficulty level of the recipe
                        String difficultyLevel = recipeTypeDao.getRecipeLevel(selectedRecipeId);
                        Log.d(TAG, "Difficulty level of recipe: " + difficultyLevel);

                        //Depending on the difficulty level, give the user points
                        switch (difficultyLevel) {
                            case "EASY":
                                giveUserPoints(50);
                                break;
                            case "MED":
                                giveUserPoints(75);
                                break;
                            case "HARD":
                                giveUserPoints(100);
                                break;
                        }
                    }
                });
            }
        });
    }

    //Sets the UI elements
    private void setRecipeInfo() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(8));
                Glide.with(FavouriteRecipeDetailActivity.this)
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

    //Creates the checkbox of ingredients
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

    private void setNotes(final String value){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etNotes.setText(value);
            }
        });
    }

    private void giveUserPoints(int points) {
        //Get user's current points
        Integer currPoints = userDao.findPointsById(MainActivity.currUserID);
        //Add points & update the user database
        Integer newPoints = currPoints + points;
        userDao.updateUserPoints(MainActivity.currUserID, newPoints);
        //Add to the database that the user has completed the recipe
        String id = selectedRecipeId + MainActivity.currUserID;
        userRecipeCompletedDao.insertAll(new UserRecipeCompleted(id, selectedRecipeId, MainActivity.currUserID));
        showToast("Congrats! You just earned 50 points!");
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