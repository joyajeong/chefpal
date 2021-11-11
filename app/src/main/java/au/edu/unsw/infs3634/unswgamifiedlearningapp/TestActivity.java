package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class TestActivity extends AppCompatActivity {
    private RecyclerView testView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testView = findViewById(R.id.test_Recycler);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //category name
        int cat_index = getIntent().getIntExtra("CAT_INDEX",0);
        getSupportActionBar().setTitle(LearnFragment.categoryList.get(cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}