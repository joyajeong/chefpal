package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserFavouriteRecipeDao {
    @Query("SELECT * FROM userFavouriteRecipe")
    List<UserFavouriteRecipe> getAll();

    @Query("SELECT * FROM userFavouriteRecipe WHERE " +
            "id LIKE :id LIMIT 1")
    UserFavouriteRecipe findById(String id);

    @Query("SELECT * FROM userFavouriteRecipe WHERE " +
            "userId LIKE :userId")
    List<UserFavouriteRecipe> findFavRecipesByUserId(String userId);

    @Query("SELECT * FROM userFavouriteRecipe WHERE " +
            "recipeId LIKE :recipeId")
    List<UserFavouriteRecipe> findFavRecipeByRecipeId(String recipeId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserFavouriteRecipe... favouriteRecipe);

    @Delete
    void delete(UserFavouriteRecipe favouriteRecipe);

    //Deletes all favourite recipe data
    @Query("DELETE FROM userFavouriteRecipe")
    void delete();
}

