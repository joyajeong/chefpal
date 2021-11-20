package au.edu.unsw.infs3634.unswgamifiedlearningapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.Model.Rank;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.R;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.RecipeInformationResult;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.User;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<User> userList;

    public RankAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, int position) {
        String name = userList.get(position).getUsername();
        int score = userList.get(position).getPoints();
        int rank = position + 1;
        holder.setData(name,score,rank);
    }

    @Override
    public int getItemCount() {
        if(userList.size()>10){
            return 10;
        }
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, rankTV, scoreTV, imgTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.name);
            rankTV= itemView.findViewById(R.id.rank);
            imgTV = itemView.findViewById(R.id.img_Text);
            scoreTV = itemView.findViewById(R.id.score);


        }
        private void setData(String name, int score, int rank){
            nameTV.setText(name);
            scoreTV.setText("Score : "+score);
            rankTV.setText("Rank - "+rank);
            imgTV.setText(name.toUpperCase().substring(0,1));
        }

    }

    public void updateRank(List<User> users) {
        Log.d("RankAdapter", "Updating recycler view");
        userList.removeAll(userList);
        userList.addAll(users);
        notifyDataSetChanged();
    }
}
