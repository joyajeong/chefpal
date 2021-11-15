package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeLevelsActivity extends AppCompatActivity {

    private int recipeId;
    private static final String TAG = "RecipeLevelsActivity";
    private TextView recipeTitle;
    private RecipeSearchResult searchResult;
    private String recipeType;
    private RecyclerView recyclerView;
    private RecipeRecyclerViewAdapter adapter;
    private int userPoints;
    public static int EASY_LIMIT = 1000;
    public static int MED_LIMIT = 3000;
    public static int HARD_LIMIT = 5000;
//    public static int EASY_LEVEL = 15;
//    public static int MED_LEVEL = 30;
//    public static int HARD_LEVEL = 45;
//    public static int NUM_RESULTS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciple_levels);

        //Getting current points TODO make a new method for this
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user").build();
        UserDao userDao = db.userDao();
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            userPoints = userDao.findPointsById(MainActivity.currUserID);
            Log.d(TAG, "Current user points: " + userPoints);
        });

        Bundle bundle = getIntent().getExtras();
        recipeType = bundle.getString("RECIPE_TYPE");
        Log.d(TAG, recipeType);

        //Initializing the level buttons
        Button btnEasy = findViewById(R.id.btnEasy);
        Button btnMed = findViewById(R.id.btnMed);
        Button btnHard = findViewById(R.id.btnHard);

        //TODO CHECK THIS LOGIC
        if (userPoints <= 1000) {
            btnMed.setEnabled(false);
            btnHard.setEnabled(false);
        } else if (userPoints <=5000) {
            btnHard.setEnabled(false);
        }

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecipeLevelsListAcitivty("EASY");
            }
        });

        btnMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecipeLevelsListAcitivty("MED");
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecipeLevelsListAcitivty("HARD");
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