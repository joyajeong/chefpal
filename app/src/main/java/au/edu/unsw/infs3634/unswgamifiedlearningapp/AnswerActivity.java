package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

public class AnswerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView answersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        //category name

        getSupportActionBar().setTitle("Answers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        answersView.setLayoutManager(layoutManager);

        //adapter
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            AnswerActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}