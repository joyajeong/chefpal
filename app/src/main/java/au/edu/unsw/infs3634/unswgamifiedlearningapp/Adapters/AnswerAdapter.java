package au.edu.unsw.infs3634.unswgamifiedlearningapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.Model.Question;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.R;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    private List<Question> questList;

    public AnswerAdapter(List<Question> questList) {
        this.questList = questList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout,parent,false);
        return new AnswerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ques = questList.get(position).getQuestion();
        String a = questList.get(position).getOptionA();
        String b = questList.get(position).getOptionB();
        String c = questList.get(position).getOptionC();
        String d = questList.get(position).getOptionD();
        int selected = questList.get(position).getSelectedAns();
        int ans = questList.get(position).getCorrectAns();
        String feedback = questList.get(position).getFeedback();

        holder.setData(position,ques,a,b,c,d,selected,ans);

    }

    @Override
    public int getItemCount() {
        return questList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView quesNo, question, optionA, optionB, optionC, optionD, result;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quesNo = itemView.findViewById(R.id.quesNo);
            question = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            result =  itemView.findViewById(R.id.result);

        }
        private void setData(int pos, String ques, String a, String b, String c, String d, int selected, int correctAns){
            quesNo.setText("QuestionNo. "+String.valueOf(pos+1));
            question.setText(ques);
            optionA.setText("A. "+a);
            optionB.setText("B. "+b);
            optionC.setText("C. "+c);
            optionD.setText("D. "+d);

            if(selected == -1){
                result.setText("Unanswered");
                result.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
            }
            else{
                if(selected == correctAns){
                    result.setText("Correct");
                    result.setTextColor(itemView.getContext().getResources().getColor(R.color.green));
                    setOptionColor(selected,R.color.green);
                }
                else{
                    result.setText("Wrong");
                    result.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                    setOptionColor(selected,R.color.red);
                }
            }

        }

        private void setOptionColor (int selected, int color){
            switch (selected){
                case 1 :
                    optionA.setTextColor(itemView.getContext().getResources().getColor(color));
                break;

                case 2 :
                    optionB.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 3 :
                    optionC.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;
                case 4 :
                    optionD.setTextColor(itemView.getContext().getResources().getColor(color));
                    break;

                default:

            }
        }

    }
}
