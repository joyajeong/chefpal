package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    LearnFragment learnFragment = new LearnFragment();
    RecipesFragment recipesFragment = new RecipesFragment();
    FavouritesFragment favouritesFragment = new FavouritesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Testing out database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "users").build();

        UserDao userDao = db.userDao();
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            userDao.insertAll(User.userData());
            Log.i("Added user", userDao.findById("joyaj").firstName);
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
            }
            return false;
        }
    };


}