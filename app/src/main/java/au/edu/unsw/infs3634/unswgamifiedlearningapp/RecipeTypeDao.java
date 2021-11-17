package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeTypeDao {
    //Gets all recipeTypes
    @Query("SELECT * FROM recipeType")
    List<RecipeType> getAll();

    //Gets recipe IDs of particular type and difficulty level
    @Query("SELECT recipeId FROM recipeType WHERE " +
            "recipeType LIKE :recipeType " +
            "AND level LIKE :level")
    List<Integer> getAllRecipeIdByTypeLevel(String recipeType, String level);

    //Gets recipetype by ID
    @Query("SELECT * FROM recipeType WHERE " +
            "recipeId LIKE :id")
    List<RecipeType> getRecipeTypeById(String id);

    //Gets difficulty level of a recipe
    @Query("SELECT level FROM recipeType WHERE " +
            "recipeId = :id")
    String getRecipeLevel(Integer id);

    //Insert RecipeType
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RecipeType... recipeTypes);

    //Delete row from recipeType
    @Delete
    void delete(RecipeType recipeType);

    //Deletes all rows in recipeType
    @Query("DELETE FROM recipeType")
    void deleteAll();
}
