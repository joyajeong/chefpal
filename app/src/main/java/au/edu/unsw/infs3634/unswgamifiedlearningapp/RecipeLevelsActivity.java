package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.UserDao;

public class RecipeLevelsActivity extends AppCompatActivity {

    private static final String TAG = "RecipeLevelsActivity";
    private String recipeType;
    private int userPoints;
    private ImageButton btnBack;
    public static int EASY_LIMIT = 1000;
    public static int MED_LIMIT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciple_levels);
        getSupportActionBar().hide();

        //Getting current user points
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user").build();
        UserDao userDao = db.userDao();
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            userPoints = userDao.findPointsById(MainActivity.currUserID);
            Log.d(TAG, "Current user points: " + userPoints);
        });

        //Receive the recipe type
        Bundle bundle = getIntent().getExtras();
        recipeType = bundle.getString("RECIPE_TYPE");
        Log.d(TAG, recipeType);

        //Initializing the level buttons
        Button btnEasy = findViewById(R.id.btnEasy);
        Button btnMed = findViewById(R.id.btnMed);
        Button btnHard = findViewById(R.id.btnHard);

        //Disable difficulty level buttons based on how many points the user has
        if (userPoints <= EASY_LIMIT) {
            btnMed.setEnabled(false);
            btnHard.setEnabled(false);
        } else if (userPoints <= MED_LIMIT) {
            btnHard.setEnabled(false);
        }

        //When Easy button is clicked
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecipeLevelsListAcitivty("EASY");
            }
        });

        //When Medium button is clicked
        btnMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecipeLevelsListAcitivty("MED");
            }
        });

        //When Hard button is clicked
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecipeLevelsListAcitivty("HARD");
            }
        });

        //When back button is clicked
        btnBack = findViewById(R.id.btnBack3);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void launchRecipeLevelsListAcitivty(String level) {
        Intent intent = new Intent(RecipeLevelsActivity.this, RecipeLevelsListActivity.class);
        intent.putExtra("DIFFICULTY_LEVEL", level);
        intent.putExtra("RECIPE_TYPE", recipeType);
        startActivity(intent);
    }

}