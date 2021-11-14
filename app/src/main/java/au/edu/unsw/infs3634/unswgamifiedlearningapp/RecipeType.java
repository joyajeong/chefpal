package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipeType")
public class RecipeType {

    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "recipeType")
    @NonNull
    public String recipeType;

    @ColumnInfo(name = "recipeId")
    @NonNull
    public Integer recipeId;

    @ColumnInfo(name = "level")
    @NonNull
    public String level;

    //Constructor
    public RecipeType(String id, String recipeType, Integer recipeId, String level) {
        this.id = id;
        this.recipeType = recipeType;
        this.recipeId = recipeId;
        this.level = level;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeLevel(String level) {
        this.level = level;
    }

    public String getRecipeLevel() {
        return level;
    }
}
