package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_categoryList;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.loadQuestions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class StartTestActivity extends AppCompatActivity {
    private TextView catName, testNo, totalQuestion, bestScore, time;
    private Button startTestB;
    private ImageView backB;
    private Dialog progressDialog;
    private TextView dialogueText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        init();

        progressDialog = new Dialog(StartTestActivity.this);
        progressDialog.setContentView(R.layout.dialogue);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogueText = progressDialog.findViewById(R.id.dialogue_text);
        dialogueText.setText("Loading...");
        progressDialog.show();

        loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                setData();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(StartTestActivity.this, "Something went wrong! Please try again!",
                        Toast.LENGTH_SHORT).show();
            }
        });

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
    private void setData(){
        //set categoryName
        catName.setText(g_categoryList.get(DbQuery.g_selected_cat_index).getName());
        //start from 0
        testNo.setText("Test No. "+ String.valueOf(DbQuery.g_selected_test_index+1));
        totalQuestion.setText(String.valueOf(DbQuery.g_quesList.size()));
        bestScore.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTopScore()));
        time.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime()));

    }

}