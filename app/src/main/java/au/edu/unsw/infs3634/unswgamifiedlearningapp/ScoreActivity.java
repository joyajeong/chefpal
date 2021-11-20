package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
    private TextView score, totalTime, totalQ, correctQ, wrongQ,unattempted;
    private Button leaderB, reattemptedB, viewAnsB;
    private long timeTaken;
    private Dialog progressDialog;
    private TextView dialogueText;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Toolbar toolbar = findViewById(R.id.toolbar);
//
//       setSupportActionBar(toolbar);
//       getSupportActionBar().setDisplayShowTitleEnabled(true);
//
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        progressDialog = new Dialog(ScoreActivity.this);
        progressDialog.setContentView(R.layout.dialogue);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogueText = progressDialog.findViewById(R.id.dialogue_text);
        dialogueText.setText("Loading...");
        progressDialog.show();




                init();
                loadData();

        //BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

                leaderB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //bottomNavigationView.setSelectedItemId(R.id.home);

                    }
                });

                viewAnsB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ScoreActivity.this, AnswerActivity.class);
                        startActivity(intent);
                    }
                });

                reattemptedB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reAttempt();

                    }
                });



                saveResult();
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
                int correctQues = 0, wrongQues = 0, unattemptedQues = 0;
                //check all questions

                for(int i =0; i<DbQuery.g_quesList.size(); i++){
                    //if default value is not changed -> unattempted
                    if(DbQuery.g_quesList.get(i).getSelectedAns() == -1){
                        unattemptedQues ++;
                    }
                    else{
                        if(DbQuery.g_quesList.get(i).getSelectedAns() == DbQuery.g_quesList.get(i).getCorrectAns()){
                            correctQues++;
                        }
                        else{
                            wrongQues++;
                        }

                    }

                }
                correctQ.setText(String.valueOf(correctQues));
                wrongQ.setText(String.valueOf(wrongQues));
                unattempted.setText(String.valueOf(unattemptedQues));
                totalQ.setText(String.valueOf(DbQuery.g_quesList.size()));
                //score in %
                finalScore = (correctQues*100)/DbQuery.g_quesList.size();
                score.setText(String.valueOf(finalScore));

                timeTaken = getIntent().getLongExtra("Time Taken",0);
                String time = String.format("%02d : %2d min",
                        //12:23 remaining, take 12 only
                        TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                        //extra 23
                        //total seconds - minute (12) in seconds
                        TimeUnit.MILLISECONDS.toSeconds(timeTaken) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))
                );

                totalTime.setText(time);

            }

            private void reAttempt(){
                for(int i = 0; i<DbQuery.g_quesList.size(); i++){
                    DbQuery.g_quesList.get(i).setSelectedAns(-1);
                    DbQuery.g_quesList.get(i).setStatus(DbQuery.NOT_VISITED);
                }
                Intent intent  = new Intent(ScoreActivity.this, StartTestActivity.class);
                startActivity(intent);
            }



            private void saveResult() {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {


                DbQuery.saveResult(finalScore, new MyCompleteListener() {

                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        progressDialog.dismiss();
                        Toast.makeText(ScoreActivity.this, "Something went wrong! Please try again!", Toast.LENGTH_SHORT).show();
                    }
                    });

                    }});

                }

            @Override
            public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()== android.R.id.home){
                    ScoreActivity.this.finish();
                }
                return super.onOptionsItemSelected(item);
            }


    }
