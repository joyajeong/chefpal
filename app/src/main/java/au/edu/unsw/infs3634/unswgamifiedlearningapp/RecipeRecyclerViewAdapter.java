package au.edu.unsw.infs3634.unswgamifiedlearningapp;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> implements Filterable{
    private List<RecipeInformationResult> mRecipes;
    private List<RecipeInformationResult> mRecipesFiltered;
    private ClickListener mClickListener;
    private static final String TAG = "RecipeRecyclerViewAdapter";
    public static final int SORT_RECIPE_NAME = 1;
    public static final int SORT_POPULARITY = 2;

    //Constructor for adapter
    public RecipeRecyclerViewAdapter(List<RecipeInformationResult> recipes, ClickListener listener) {
        this.mRecipes = recipes;
        this.mRecipesFiltered = recipes;
        this.mClickListener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //If there is no filter query, the filtered list is the default list
                    mRecipesFiltered = mRecipes;
                } else {
                    List<RecipeInformationResult> filteredList = new ArrayList<>();
                    Log.d(TAG, "Search query: " + charString);

                    for (RecipeInformationResult s : mRecipes) {
                        //Filters through the recipe names
                        if (!filteredList.contains(s) &&
                                s.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(s);
                        }
                    }
                    mRecipesFiltered = filteredList;
                }
                FilterResults filterResult = new FilterResults();
                filterResult.values = mRecipesFiltered;
                return filterResult;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mRecipesFiltered = (List<RecipeInformationResult>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    // Allows clicks events to be caught
    public interface ClickListener {
        void onRecipeClick(View view, String id);
    }

    @NonNull
    @Override
    public RecipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_row, parent, false);
        return new ViewHolder(view, mClickListener);
    }

    //Associate the data with the view holder for a given position in the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull RecipeRecyclerViewAdapter.ViewHolder holder, int position) {
        final RecipeInformationResult recipe = mRecipesFiltered.get(position);
        holder.recipeTitle.setText(recipe.getTitle());
        holder.recipeTitle.setSelected(true);
        holder.recipeTime.setText(String.valueOf(recipe.getReadyInMinutes()));
        holder.itemView.setTag(recipe.getId());
        Glide.with(holder.image.getContext())
                .load(recipe.getImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mRecipesFiltered.size();
    }

    //Extend the ViewHolder to implement ClickListener
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        TextView recipeTitle, recipeTime;
        ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.ivImage);
            recipeTitle = itemView.findViewById(R.id.tvRecipeTitle);
            recipeTime = itemView.findViewById(R.id.tcRecipeTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRecipeClick(view, (String) String.valueOf(view.getTag()));
        }
    }

    //Sorts the list by either recipe name or popularity
    public void sort(final int sortMethod) {
        if(mRecipesFiltered.size() > 0) {
            Collections.sort(mRecipesFiltered, new Comparator<RecipeInformationResult>() {
                @Override
                public int compare(RecipeInformationResult r1, RecipeInformationResult r2) {
                    if (sortMethod == SORT_RECIPE_NAME) {
                        //Sort by recipe name
                        Log.d(TAG, "Sorting by Recipe name");
                        return r1.getTitle().toLowerCase().compareTo(r2.getTitle().toLowerCase());
                    } else if (sortMethod == SORT_POPULARITY) {
                        //Sort by popularity
                        Log.d(TAG, "Sorting by ratings");
                        return -(String.valueOf(r1.getAggregateLikes())).compareTo(String.valueOf((r2.getAggregateLikes())));
                    }
                    //Default sort by recipe name
                    return r1.getTitle().toLowerCase().compareTo(r2.getTitle().toLowerCase());
                }
            });
        }
        notifyDataSetChanged();
    }

    public void updateRecipeList(List<RecipeInformationResult> recipes) {
        Log.d(TAG, "Updating recycler view");
        mRecipesFiltered.removeAll(mRecipes);
        mRecipesFiltered.addAll(recipes);
        notifyDataSetChanged();
    }
}
