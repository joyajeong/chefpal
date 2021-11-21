package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_userList;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_usersCount;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.myPerformance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executors;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.UserDao;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.QuizAdapters.RankAdapter;


public class HomeFragment extends Fragment {
    private TextView tvPoints, tvProgress;
    private RecyclerView usersView;
    private RankAdapter adapter;
    private static String TAG = "HomeFragment";
    private ImageView hat1, hat2, hat3;
    private UserDao userDao;
    private int progress;
    public static int EASY_LIMIT = 1000;
    public static int MED_LIMIT = 5000;
    private Dialog progressDialog;
    private TextView dialogueText;
    private int currPoint;
    private ImageButton logoutB;

    public HomeFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setElevation(0);

        //Get user points from Firebase database
        DbQuery.getUserPoints(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                currPoint = DbQuery.score.intValue();
                Log.d(TAG, "curr score: " + currPoint);

                //Personalise home page heading
                ((MainActivity)getActivity()).getSupportActionBar().setTitle("Welcome, " + DbQuery.name);
                Log.d(TAG, "name: " + DbQuery.name);
            }
            @Override
            public void onFailure() {
            }
        });

        //Initialise view elements
        initView(view);

        //Set recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        //Adapter
        adapter = new RankAdapter(g_userList);
        usersView.setAdapter(adapter);

        //User database
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "user").fallbackToDestructiveMigration().build();
        userDao = db.userDao();

        //Update number of hats depending on user's level
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                int points = userDao.findPointsById(MainActivity.currUserID);
                if (points <= EASY_LIMIT) {
                    //If user has less points than the easy limit, only has one hats
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hat2.setVisibility(View.INVISIBLE);
                            hat3.setVisibility(View.INVISIBLE);
                        }
                    });
                } else if (points <= MED_LIMIT) {
                    //If user has less points than the med limit, has two hats
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hat2.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialogue);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogueText = progressDialog.findViewById(R.id.dialogue_text);
        dialogueText.setText("Loading...");
        progressDialog.show();

        usersView.setAdapter(adapter);

        DbQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
                if(DbQuery.myPerformance.getScore() != 0){
                    if(!DbQuery.isMeOnTopList){
                        calculateRank();
                    }
                }
                progressDialog.dismiss();

            }
            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong! Please try again!",
                        Toast.LENGTH_SHORT).show();
            }
        });

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

        //Setting header progress
        calculateProgress();
        return view;
    }

    private void initView(View view){
        usersView = view.findViewById(R.id.user_view);
        hat1 = view.findViewById(R.id.hatDark1);
        hat2 = view.findViewById(R.id.hatDark2);
        hat3 = view.findViewById(R.id.hatDark3);
        tvPoints = view.findViewById(R.id.tvPoints);
        tvProgress = view.findViewById(R.id.tvProgress);
        logoutB = view.findViewById(R.id.logoutB);
    }


    private int calculateProgress() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                int points = userDao.findPointsById(MainActivity.currUserID);
                if (points <= EASY_LIMIT) {
                    //User is is in easy level
                    progress = EASY_LIMIT - points;
                    setProgessText(points, progress, "MEDIUM");
                } else if (points <= MED_LIMIT) {
                    //User is in med level
                    progress = MED_LIMIT - points;
                    setProgessText(points, progress, "HARD");
                }
            }
        });
        return progress;
    }

    private void setProgessText(int points, int progress, String level) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvPoints.setText(String.valueOf(points));
                tvProgress.setText(progress + " points to get another Chef Hat and unlock " +
                        level +" recipes!");
            }
        });
    }

    private void calculateRank(){
        //get last element
        int lowTopScore = g_userList.get(g_userList.size()-1).getScore();

        //how many remaining
        int remaining_slots = g_usersCount - 10;

        int mySlot = (myPerformance.getScore()*remaining_slots)/lowTopScore;

        int rank;

        if (lowTopScore != myPerformance.getScore()){
            rank = g_usersCount - mySlot;
        } else {
            rank = 11;
        }
        myPerformance.setRank(rank);
    }

}