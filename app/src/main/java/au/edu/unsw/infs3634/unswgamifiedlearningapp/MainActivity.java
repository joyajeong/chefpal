package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    LearnFragment learnFragment = new LearnFragment();
    RecipesFragment recipesFragment = new RecipesFragment();
    FavouritesFragment favouritesFragment = new FavouritesFragment();
    AccountFragment accountFragment = new AccountFragment();

    private FirebaseAuth mAuth;
    private static String TAG = "MainActivity";
    public static String currUserID, currUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "Current user id: " +  currUserID);

        //User database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user").fallbackToDestructiveMigration().build();
        UserDao userDao = db.userDao();
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            //If user doesn't already exist add user to database
            if (userDao.findById(mAuth.getCurrentUser().getUid()) == null) {
                String username = getUsername(mAuth.getCurrentUser().getEmail());
                userDao.insertAll(new User(mAuth.getCurrentUser().getUid(), 0, username));
                Log.d(TAG, "Added new user!");
            } else {
                currUsername = userDao.findById(currUserID).getUsername();
                Log.d(TAG, currUsername + " has logged in");
            }
            Log.d(TAG, "Current user points: " +  userDao.findPointsById(currUserID));

        });

        //Setting up bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(changeListener);
        bottomNavigationView.setSelectedItemId(R.id.home);

    }

    //Changes fragments
    NavigationBarView.OnItemSelectedListener changeListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;

                case R.id.learn:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, learnFragment).commit();
                    return true;

                case R.id.recipes:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, recipesFragment).commit();
                    return true;

                case R.id.favourites:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, favouritesFragment).commit();
                    return true;

                case R.id.account:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment).commit();
                    return true;
            }
            return false;
        }
    };

    private String getUsername(String email) {
        String[] parts = mAuth.getCurrentUser().getEmail().split("\\@");
        String username = parts[0];
        Log.d(TAG, "username: " + username);

        return username;
    }

}