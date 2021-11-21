package au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.UserFavouriteRecipe;

@Dao
public interface UserFavouriteRecipeDao {
    //Get all from userFavouriteRecipe
    @Query("SELECT * FROM userFavouriteRecipe")
    List<UserFavouriteRecipe> getAll();

    //Get userFavouriteRecipe using ID
    @Query("SELECT * FROM userFavouriteRecipe WHERE " +
            "id LIKE :id LIMIT 1")
    UserFavouriteRecipe findById(String id);

    //Get userFavouriteRecipes using user ID
    @Query("SELECT * FROM userFavouriteRecipe WHERE " +
            "userId LIKE :userId")
    List<UserFavouriteRecipe> findFavRecipesByUserId(String userId);

    //Get userFavouriteRecipes using recipe ID
    @Query("SELECT * FROM userFavouriteRecipe WHERE " +
            "recipeId LIKE :recipeId")
    List<UserFavouriteRecipe> findFavRecipeByRecipeId(String recipeId);

    //Get recipe IDs that users have favourited by user ID
    @Query("SELECT recipeId FROM userFavouriteRecipe WHERE " +
            "userId LIKE :userId")
    List<Integer> findFavRecipeIdByUserId(String userId);

    //Update notes
    @Query("UPDATE userFavouriteRecipe SET notes = :notes WHERE " +
            "id LIKE :id")
    void updateNotes(String notes, String id);

    //Insert favouriteRecipe
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserFavouriteRecipe... favouriteRecipe);

    //Delete favouriteRecipe
    @Delete
    void delete(UserFavouriteRecipe favouriteRecipe);

    //Deletes all favourite recipes
    @Query("DELETE FROM userFavouriteRecipe")
    void delete();
}

