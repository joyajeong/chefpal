package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {
    //Maybe change back to FavouriteFragment?

    private static String TAG = "AccountFragment";
    private LinearLayout logoutB, learderBoard, profileButton, bookmark;
    private TextView profile_img_text, name, score, rank;
    private BottomNavigationView bottomNav;




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
        //String userName = DbQuery.myProfile.getName();
        //profile_img_text.setText();

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
                Intent intent = new Intent(getContext(),Favorite.class);
                startActivity(intent);

            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // profile activity
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
}

