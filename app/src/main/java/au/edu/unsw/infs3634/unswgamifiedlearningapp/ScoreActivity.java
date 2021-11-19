package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    private TextView score, totalTime, totalQ, correctQ, wrongQ,unattempted;
    private Button leaderB, reattemptedB, viewAnsB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        init();
        loadData();
    }

    private void init(){
        score = findViewById(R.id.score);
        totalQ = findViewById(R.id.totalQ);
        totalTime = findViewById(R.id.totalTime);
        correctQ = findViewById(R.id.correctQ);
        wrongQ = findViewById(R.id.wrongQ);
        unattempted = findViewById(R.id.unattempted);
        leaderB = findViewById(R.id.checkRankB);
        reattemptedB = findViewById(R.id.reattemptB);
        viewAnsB = findViewById(R.id.viewAnsB);
    }

    private void loadData(){
        int correctQ = 0, wrongQ = 0, unattempted = 0;
        //

    }


}