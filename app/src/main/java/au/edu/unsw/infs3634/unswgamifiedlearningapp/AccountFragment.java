package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    //Maybe change back to FavouriteFragment?

    private static String TAG = "AccountFragment";
    private String currUserID;
    private TextView tvFavouriteRecipes;
    private List<UserFavouriteRecipe> favRecipes;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    //Do any logic here
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvFavouriteRecipes = getView().findViewById(R.id.tvFavouriteRecipes);

        //UserFavouriteRecipe Database
        AppDatabase db = Room.databaseBuilder(getActivity(),
                AppDatabase.class, "userFavouriteRecipe").fallbackToDestructiveMigration().build();

        currUserID = MainActivity.currUserID;
        UserFavouriteRecipeDao userFavouriteRecipeDao = db.userFavouriteRecipeDao();

        //Checking how many recipes they have liked
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            favRecipes = userFavouriteRecipeDao.findFavRecipesByUserId(currUserID);
            Log.d(TAG, "Current favourite recipes: " + favRecipes.size());
            setText(tvFavouriteRecipes, "Number of favourite recipes: " + favRecipes.size());
        });

    }

    private void setText(final TextView text,final String value){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
}