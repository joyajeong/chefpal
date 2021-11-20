package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_userList;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_usersCount;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.myPerformance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class AccountFragment extends Fragment {
    //Maybe change back to FavouriteFragment?

    private static String TAG = "AccountFragment";
    private LinearLayout logoutB, learderBoard, profileButton, bookmark;
    private TextView profile_img_text, name, score, rank;
    private BottomNavigationView bottomNav;
    private Dialog progressDialog;
    private TextView dialogueText;






    public AccountFragment() {
        // Required empty public constructor
        Log.d(TAG, "DF");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initViews(view);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialogue);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogueText = progressDialog.findViewById(R.id.dialogue_text);
        dialogueText.setText("Loading...");
        progressDialog.show();

        String userName = DbQuery.myProfile.getName();
        profile_img_text.setText(userName.toUpperCase().substring(0,1));
        name.setText(userName);
        score.setText(String.valueOf(DbQuery.myPerformance.getScore()));

        if(DbQuery.g_userList.size() == 0){
            DbQuery.getTopUsers(new MyCompleteListener() {
                @Override
                public void onSuccess() {

                    if(myPerformance.getScore() != 0){
                        if(DbQuery.isMeOnTopList){
                            calaculateRank();
                        }
                        score.setText("Score : " + myPerformance.getScore());
                        rank.setText("Rank - "+ myPerformance.getRank());

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
        }


        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("27382583312-3l51j2snafns9m81taca1fdtoc440udf.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

                mGoogleSignInClient.signOut().addOnCompleteListener((task) -> {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();

                });
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //favorites
                Intent intent = new Intent(getContext(), FavoriteActivity.class);
                startActivity(intent);

            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // profile activity#$%^&*()!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        });

        learderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // leaderBoard!!!!!, 28-9:30

            }
        });

        return view;
    }

    private void initViews(View view){
        logoutB = view.findViewById(R.id.logout);
        profile_img_text = view.findViewById(R.id.profile_img_text);
        bookmark = view.findViewById(R.id.bookMark);
        learderBoard = view.findViewById(R.id.leaderButton);
        name = view.findViewById(R.id.profileName);
        profileButton = view.findViewById(R.id.profileButton);
        score = view.findViewById(R.id.totalScore);
        rank = view.findViewById(R.id.rank);
        bottomNav = getActivity().findViewById(R.id.bottomNavigationView);
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

