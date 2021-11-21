package au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.UserRecipeCompleted;

@Dao
public interface UserRecipeCompletedDao {
    //Gets all userRecipeCompleted
    @Query("SELECT * FROM userRecipeCompleted")
    List<UserRecipeCompleted> getAll();

    //Gets the recipes the user has completed
    @Query("SELECT * FROM userRecipeCompleted " +
            "WHERE recipeId = :recipeId " +
            "AND userid LIKE :userId")
    List<UserRecipeCompleted> completedRecipesByUser (String userId, Integer recipeId);

    //Inserts recipes user has completed
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserRecipeCompleted... userRecipeCompleted);

    //Delete user completed recipes
    @Delete
    void delete(UserRecipeCompleted userRecipeCompleted);

    //Delete all rows from userRecipeCompleted
    @Query("DELETE FROM userRecipeCompleted")
    void deleteAll();
}

