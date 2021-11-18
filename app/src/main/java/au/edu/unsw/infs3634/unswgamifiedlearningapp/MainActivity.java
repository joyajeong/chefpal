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
    public static String currUserID;

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
            //if user doesn't already exist add user to database TODO - need to check if it actually works
            if (userDao.findById(mAuth.getCurrentUser().getUid()) == null) {
                userDao.insertAll(new User(mAuth.getCurrentUser().getUid(), 0, mAuth.getCurrentUser().getDisplayName()));
                Log.d(TAG, "Added new user named: " + mAuth.getCurrentUser().getDisplayName() + "!");
            }
            Log.d(TAG, "Current user points: " +  userDao.findPointsById(currUserID));
        });

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<User> orderedUsers = userDao.getOrderedUsersByPoints();
                if (orderedUsers.size() > 0 && orderedUsers != null) {
                    for (User user : orderedUsers) {
                        Log.d(TAG, user.getName() + "has " + user.getPoints() + " points");
                    }
                }
            }
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


}