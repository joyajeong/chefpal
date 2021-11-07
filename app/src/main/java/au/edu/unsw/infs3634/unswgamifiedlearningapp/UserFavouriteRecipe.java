package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "userFavouriteRecipe")
public class UserFavouriteRecipe {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "userId")
    public String userId;

    @ColumnInfo(name = "recipeId")
    public Integer recipeId;

    //Constructor
    public UserFavouriteRecipe(String id, String userId, Integer recipeId) {
        this.id = id;
        this.userId = userId;
        this.recipeId = recipeId;
    }


    //Create getter & setter methods
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }




}