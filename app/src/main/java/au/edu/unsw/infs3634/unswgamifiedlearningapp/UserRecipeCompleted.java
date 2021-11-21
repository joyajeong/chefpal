package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "userRecipeCompleted")
public class UserRecipeCompleted {
    @ColumnInfo(name = "id")
    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "recipeId")
    @NonNull
    public Integer recipeId;

    @ColumnInfo(name = "userId")
    @NonNull
    public String userId;

    public UserRecipeCompleted(String id, Integer recipeId, String userId) {
        this.id = id;
        this.recipeId = recipeId;
        this.userId = userId;

    }
}
