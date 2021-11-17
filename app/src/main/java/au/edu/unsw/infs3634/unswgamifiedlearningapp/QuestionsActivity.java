package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView questionView;
    private TextView questionID, timer, catName;
    private Button submitB,markB, clearSelectionB;
    private ImageButton previousQ, nextQ;
    private ImageView questionListB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        init();
        QuestionAdapter questionAdapter = new QuestionAdapter(DbQuery.g_quesList);
        questionView.setAdapter(questionAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionView.setLayoutManager(layoutManager);

    }


    private void init(){
        questionView = findViewById(R.id.questionView);
        questionID = findViewById(R.id.tv_questionID);
        timer = findViewById(R.id.tv_timer);
        catName = findViewById(R.id.quizCatName);
        submitB = findViewById(R.id.submitB);
        markB = findViewById(R.id.markB);
        clearSelectionB = findViewById(R.id.clearSelection);
        previousQ = findViewById(R.id.previousQ);
        nextQ = findViewById(R.id.nextQ);
        questionListB = findViewById(R.id.questionsList_gridB);

    }
}