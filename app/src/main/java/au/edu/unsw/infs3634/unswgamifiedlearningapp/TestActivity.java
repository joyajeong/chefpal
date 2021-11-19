package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.Adapters.TestAdapter;

public class TestActivity extends AppCompatActivity {
    private RecyclerView testView;
    private Toolbar toolbar;
    private TestAdapter adapter;
    private Dialog progressDialog;
    private TextView dialogueText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testView = findViewById(R.id.test_Recycler);
        toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        //category name

        getSupportActionBar().setTitle(DbQuery.g_categoryList.get(DbQuery.g_selected_cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new Dialog(TestActivity.this);
        progressDialog.setContentView(R.layout.dialogue);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogueText = progressDialog.findViewById(R.id.dialogue_text);
        dialogueText.setText("Loading...");
        progressDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);

        //testDATA
        DbQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                //setAdapter
                adapter = new TestAdapter(DbQuery.g_testList);
                testView.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(TestActivity.this, "Something went wrong! Please try again!",
                        Toast.LENGTH_SHORT).show();

            }
        });

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