package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(),QuestionsActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        private void setData(int pos, int progress){
            //assign value to variables
            testNo.setText("Test No: "+ String.valueOf(pos + 1));
            topScore.setText(String.valueOf(progress)+" %");
            testProgress.setProgress(progress);

        }
    }
}
