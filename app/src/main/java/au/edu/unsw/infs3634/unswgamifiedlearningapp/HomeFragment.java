package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_userList;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_usersCount;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.myPerformance;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.Adapters.RankAdapter;


public class HomeFragment extends Fragment {
    private TextView myImgTextTV, tvPoints, tvProgress;
    private RecyclerView usersView;
    private RankAdapter adapter;
    private List<User> initialList = new ArrayList<>();
    private static String TAG = "HomeFragment";
    private List<User> orderedUsers = new ArrayList<>();
    private ImageView hat1, hat2, hat3;
    private UserDao userDao;
    private int progress;
    public static int EASY_LIMIT = 1000;
    public static int MED_LIMIT = 5000;
    private Dialog progressDialog;
    private TextView dialogueText;
    private int currPoint;

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

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Categories");

        DbQuery.getUserPoints(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                currPoint = DbQuery.score.intValue();
                Log.d(TAG, "curr score: " + currPoint);
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

        //Get an ordered list of users based on their points
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                orderedUsers = userDao.getOrderedUsersByPoints();
//                if (orderedUsers.size() > 0 && orderedUsers != null) {
//                    for (User user : orderedUsers) {
//                        Log.d(TAG, user.getUsername() + " has " + user.getPoints() + " points");
//                    }
//                    Log.d(TAG, "Number of users: " + userDao.getAll().size());
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            adapter.updateRank(orderedUsers);
//                        }
//                    });
//                }
//            }
//        });

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

        if(lowTopScore != myPerformance.getScore()){
            rank = g_usersCount - mySlot;
        }
        else{
            rank = 11;
        }
        myPerformance.setRank(rank);

    }

}