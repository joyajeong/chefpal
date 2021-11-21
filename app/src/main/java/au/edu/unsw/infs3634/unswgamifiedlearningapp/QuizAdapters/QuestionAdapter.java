package au.edu.unsw.infs3634.unswgamifiedlearningapp.QuizAdapters;

import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.ANSWERED;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.REVIEW;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.UNANSWERED;
import static au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery.g_quesList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.DbQuery;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.QuizModel.Question;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private List<Question> questionList;

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);


    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question;
        private Button optionA, optionB, optionC, optionD, prevSelectedB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tv_q);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            prevSelectedB = null;
        }

        private void setData(final int pos){
            question.setText(questionList.get(pos).getQuestion());
            optionA.setText(questionList.get(pos).getOptionA());
            optionB.setText(questionList.get(pos).getOptionB());
            optionC.setText(questionList.get(pos).getOptionC());
            optionD.setText(questionList.get(pos).getOptionD());

            setOption(optionA,1,pos);
            setOption(optionB,2,pos);
            setOption(optionC,3,pos);
            setOption(optionD,4,pos);


            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectOption(optionA,1,pos);

                }
            });

            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionB,2,pos);

                }
            });

            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionC,3,pos);

                }
            });

            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionD,4,pos);

                }
            });
        }

        private void selectOption(Button btn, int option_num, int quesID){
            //not select yet
            if(prevSelectedB == null){
                //change background, set optionId to selectedAns
                btn.setBackgroundResource(R.drawable.selected_button);
                DbQuery.g_quesList.get(quesID).setSelectedAns(option_num);
                changeStatus(quesID, ANSWERED);
                prevSelectedB = btn;
            }
            else{
                if(prevSelectedB.getId() == btn.getId()){
                    //selected -> unselected
                    btn.setBackgroundResource(R.drawable.unselected_button);
                    DbQuery.g_quesList.get(quesID).setSelectedAns(-1);
                    changeStatus(quesID, UNANSWERED);
                    prevSelectedB = null;
                }
                else{
                    prevSelectedB.setBackgroundResource(R.drawable.unselected_button);
                    btn.setBackgroundResource(R.drawable.selected_button);
                    DbQuery.g_quesList.get(quesID).setSelectedAns(option_num);
                    changeStatus(quesID, ANSWERED);
                    prevSelectedB = btn;
                }
            }
        }

        private void changeStatus(int id, int status){
            if( g_quesList.get(id).getStatus() != REVIEW){
                g_quesList.get(id).setStatus(status);
            }
        }

        private void setOption (Button btn, int option_num, int quesID){
            if(DbQuery.g_quesList.get(quesID).getSelectedAns() == option_num){
                btn.setBackgroundResource(R.drawable.selected_button);
            }
            else{
                btn.setBackgroundResource(R.drawable.unselected_button);
            }
        }

    }
}
