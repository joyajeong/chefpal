package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_categoryList;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_quesList;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_selected_cat_index;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_selected_test_index;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_testList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView questionView;
    private TextView questionID, timer, catName;
    private Button submitB,markB, clearSelectionB;
    private ImageButton previousQ, nextQ;
    private ImageView questionListB;
    private int questID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        init();
        QuestionAdapter questionAdapter = new QuestionAdapter(g_quesList);
        questionView.setAdapter(questionAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionView.setLayoutManager(layoutManager);

        setSnapHelper();
        setClickListener();
        startTimer();

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
        questID = 0;
        questionID.setText("1/"+String.valueOf(g_quesList.size()));
        catName.setText(g_categoryList.get(g_selected_cat_index).getName());


    }
    private void setSnapHelper(){
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionView);

        questionView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //get current view
                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                //get pos
                questID = recyclerView.getLayoutManager().getPosition(view);
                questionID.setText(String.valueOf(questID + 1)+"/"+String.valueOf(g_quesList.size()));

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }
    private void setClickListener(){

        previousQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //first question?
                if(questID > 0){
                    questionView.smoothScrollToPosition(questID-1);
                }
            }
        });

        nextQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //last question?
                if(questID<g_quesList.size()-1){
                    questionView.smoothScrollToPosition(questID +1);
                }

            }
        });

    }
    private void startTimer(){
        //getTime,convert minutes to milliseconds
        long totalTime = g_testList.get(g_selected_test_index).getTime()*60*1000;


        CountDownTimer countDownTimer = new CountDownTimer(totalTime + 1000,1000) {
            //every second, it will call onTick, change time display
            @Override
            public void onTick(long remainingTime) {
                // 12 : 23 min
                String time = String.format("%02d : %2d min",
                        //12:23 remaining, take 12 only
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        //extra 23
                        //total seconds - minute (12) in seconds
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                        );
                timer.setText(time);

            }

            @Override
            public void onFinish() {
                //finish, go to next activity

            }
        };
        countDownTimer.start();


    }
}