package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

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
        getSupportActionBar().setTitle(DbQuery.g_categoryList.get(cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);
        //testDATA
        loadTestData();
        //setAdapter
        TestAdapter adapter = new TestAdapter(testList);
        testView.setAdapter(adapter);
    }

//    private void loadTestData(){
//        testList = new ArrayList<>();
//
//        testList.add(new TestQuestion("1",50,20));
//        testList.add(new TestQuestion("2",80,40));
//        testList.add(new TestQuestion("3",10,10));
//        testList.add(new TestQuestion("4",20,10));
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}