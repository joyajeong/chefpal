package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_userList;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_usersCount;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.myPerformance;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.Adapters.RankAdapter;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.Model.Rank;


public class HomeFragment extends Fragment {
    private TextView totalUsersTV,myImgTextTV, myScoreTV, myRankTV;
    private RecyclerView usersView;
    private RankAdapter adapter;
    private Dialog progressDialog;
    private TextView dialogueText;



    public HomeFragment() {
        // Required empty public constructor
    }



//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("LeaderBoard");

        initView(view);

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialogue);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogueText = progressDialog.findViewById(R.id.dialogue_text);
        dialogueText.setText("Loading...");
        progressDialog.show();

        //set recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        //adapter
        adapter = new RankAdapter(DbQuery.g_userList);

        usersView.setAdapter(adapter);

        DbQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
                if(myPerformance.getScore() != 0){
                    if(DbQuery.isMeOnTopList){
                        calaculateRank();
                    }
                     myScoreTV.setText("Score : " + myPerformance.getScore());
                    myRankTV.setText("Rank - "+ myPerformance.getRank());

                }
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong! Please try again!",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();

            }
        });

        totalUsersTV.setText("Total Users : "+ DbQuery.g_usersCount);
        return  view;

    }

    private void initView(View view){

    totalUsersTV =  view.findViewById(R.id.totalUsers);
    myImgTextTV = view.findViewById(R.id.imgText);
    myScoreTV = view.findViewById(R.id.totalScore);
    myRankTV = view.findViewById(R.id.rank);
    usersView = view.findViewById(R.id.user_view);

}

private void calaculateRank(){
        //get last element
    int lowTopScore = g_userList.get(g_userList.size()-1).getScore();

        //how many remaining
    int remaining_slots =g_usersCount - 10;

    int mySlot = (myPerformance.getScore()*remaining_slots)/lowTopScore;

    int rank;

    if(lowTopScore != myPerformance.getScore()){
        rank = g_usersCount - mySlot;
    }
    else{
        rank = 11;
    }

    myPerformance.setScore(rank);

}


}