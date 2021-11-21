package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.QuizAdapters.CategoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearnFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LearnFragment extends Fragment {


    public LearnFragment() {
        // Required empty public constructor
    }

    private GridView catView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Quizzes");
        ((MainActivity)getActivity()).getSupportActionBar().setElevation(0);

        catView = view.findViewById(R.id.cat_Grid);
        //loadCategories();
        //Adapter
        CategoryAdapter adapter = new CategoryAdapter(DbQuery.g_categoryList);
        catView.setAdapter(adapter);


        return view;
    }

//    private void loadCategories(){
//        categoryList.clear();
//        //will fetch this data from firebase
//        // 3 category for now
//        categoryList.add(new CategoryQuiz("1","Kitchen Safety",4));
//        categoryList.add(new CategoryQuiz("2","Food Safety",4));
//        categoryList.add(new CategoryQuiz("3","Nutrition",4));
//    }
}