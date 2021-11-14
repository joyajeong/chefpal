package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavouriteRecipeDetailActivity extends AppCompatActivity {

    private static String TAG = "FavouriteRecipeDetailActivity";
    private EditText etNotes;
    private Button btnNotes, btnComplete;
    private RecipeInformationResult selectedRecipe;
    private Integer selectedRecipeId;
    private UserFavouriteRecipe userFavRecipe;
    private String notes;
    private String id;
    private boolean completed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_recipe_detail);

        Bundle bundle = getIntent().getExtras();
        selectedRecipeId = Integer.parseInt(bundle.getString("RECIPE_ID"));
        Log.d(TAG, "Selected recipe id: " + selectedRecipeId);
        id = MainActivity.currUserID + selectedRecipeId;

        etNotes = findViewById(R.id.etNotes);
        Editable newNotes = etNotes.getText();
        Log.d(TAG, "new note: " + newNotes);

        //User database
        AppDatabase dbU = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user").fallbackToDestructiveMigration().build();
        UserDao userDao = dbU.userDao();

        //UserFavouriteRecipe Database
        AppDatabase db = Room.databaseBuilder(this,
                AppDatabase.class, "userFavouriteRecipe").fallbackToDestructiveMigration().build();
        UserFavouriteRecipeDao userFavouriteRecipeDao = db.userFavouriteRecipeDao();

        //Getting the selected recipe & its notes
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            userFavRecipe = userFavouriteRecipeDao.findById(id);
            Log.d(TAG, "Current selected recipe: " + userFavRecipe.getId());
            notes = userFavouriteRecipeDao.findById(id).getNotes();
            Log.d(TAG, "notes: " + notes);
            setNotes(String.valueOf(notes));
        });

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

        //UserCompletedRecipe Database
        AppDatabase dbURC = Room.databaseBuilder(this,
                AppDatabase.class, "userRecipeCompleted").fallbackToDestructiveMigration().build();
        UserRecipeCompletedDao userRecipeCompletedDao = dbURC.userRecipeCompletedDao();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<UserRecipeCompleted> completedRecipes = userRecipeCompletedDao.completedRecipesByUser(MainActivity.currUserID, selectedRecipeId);
                if (completedRecipes.size() == 0) {
                    //The user has not completed the recipe
                    Log.d(TAG, "user has not completed the recipe already");
                } else {
                    completed = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                                //TODO: make a method for this
                                Integer currPoints = userDao.findPointsById(MainActivity.currUserID);
                                Integer newPoints = currPoints + 50;
                                userDao.updateUserPoints(MainActivity.currUserID, newPoints);

                                String id = selectedRecipeId + MainActivity.currUserID;
                                userRecipeCompletedDao.insertAll(new UserRecipeCompleted(id, selectedRecipeId, MainActivity.currUserID));
                                showToast("Congrats! You just earned 50 points!");
                                break;
                            case "MED":
                                break;
                            case "HARD":
                                break;
                        }
                    }
                });
            }
        });
    }

    private void setNotes(final String value){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etNotes.setText(value);
            }
        });
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