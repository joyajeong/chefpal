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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavouriteRecipeDetailActivity extends AppCompatActivity {

    private static String TAG = "FavouriteRecipeDetailActivity";
    private EditText etNotes;
    private Button btnNotes;
    private RecipeInformationResult selectedRecipe;
    private Integer selectedRecipeId;
    private UserFavouriteRecipe userFavRecipe;
    private String notes;
    private String id;

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