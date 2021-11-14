package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserRecipeCompletedDao {
    @Query("SELECT * FROM userRecipeCompleted")
    List<UserRecipeCompleted> getAll();

    //Gets the recipes the user has completed
    @Query("SELECT * FROM userRecipeCompleted " +
            "WHERE recipeId = :recipeId " +
            "AND userid LIKE :userId")
    List<UserRecipeCompleted> completedRecipesByUser (String userId, Integer recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserRecipeCompleted... userRecipeCompleted);

    @Delete
    void delete(UserRecipeCompleted userRecipeCompleted);

    @Query("DELETE FROM userRecipeCompleted")
    void deleteAll();
}

