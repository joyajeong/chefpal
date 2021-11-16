package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StartTestActivity extends AppCompatActivity {
    private TextView catName, testNo, totalQuestion, bestScore,time;
    private Button startTestB;
    private ImageView backB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
        init();

    }
    private void init(){
        catName = findViewById(R.id.st_CatName);
        testNo = findViewById(R.id.st_TestNo);
        totalQuestion = findViewById(R.id.totalQuestion);
        bestScore = findViewById(R.id.bestScore);
        time =  findViewById(R.id.time);
        startTestB =  findViewById(R.id.startTest);
        backB =  findViewById(R.id.st_back);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTestActivity.this.finish();
            }
        });


        startTestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTestActivity.this, QuestionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}