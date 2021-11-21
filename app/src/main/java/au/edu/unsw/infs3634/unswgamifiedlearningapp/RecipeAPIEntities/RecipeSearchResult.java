package au.edu.unsw.infs3634.unswgamifiedlearningapp.RecipeAPIEntities;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeSearchResult {

    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("results")
    @Expose
    private List<RecipeResults> recipeResults = null;
    @SerializedName("totalResults")
    @Expose
    private Integer totalResults;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<RecipeResults> getResults() {
        return recipeResults;
    }

    public void setResults(List<RecipeResults> recipeResults) {
        this.recipeResults = recipeResults;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

}