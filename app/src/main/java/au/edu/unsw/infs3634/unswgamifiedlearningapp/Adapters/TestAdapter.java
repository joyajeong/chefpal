package au.edu.unsw.infs3634.unswgamifiedlearningapp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.Model.TestQuestion;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.R;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.StartTestActivity;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private List<TestQuestion> testList;

    public TestAdapter(List<TestQuestion> testList) {
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        int progress = testList.get(position).getTopScore();
        holder.setData(position,progress);
    }

    @Override
    public int getItemCount() {

        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView testNo;
        private TextView topScore;
        private ProgressBar testProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            testNo = itemView.findViewById(R.id.testNo);
            topScore = itemView.findViewById(R.id.scoreText);
            testProgress = itemView.findViewById(R.id.testProgress);

        }

        private void setData(int pos, int progress){

            //assign value to variables
            testNo.setText("Test No: "+ String.valueOf(pos + 1));
            topScore.setText(String.valueOf(progress)+" %");
            testProgress.setProgress(progress);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Position is the test index
                    DbQuery.g_selected_test_index = pos;
                    Intent intent = new Intent(itemView.getContext(), StartTestActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
