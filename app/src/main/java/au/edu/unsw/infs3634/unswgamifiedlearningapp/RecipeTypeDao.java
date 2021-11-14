package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeTypeDao {
    @Query("SELECT * FROM recipeType")
    List<RecipeType> getAll();

    @Query("SELECT recipeId FROM recipeType WHERE " +
            "recipeType LIKE :recipeType " +
            "AND level LIKE :level")
    List<Integer> getAllRecipeIdByTypeLevel(String recipeType, String level);

    @Query("SELECT * FROM recipeType WHERE " +
            "recipeId LIKE :id")
    List<RecipeType> getRecipeTypeById(String id);

    @Query("SELECT level FROM recipeType WHERE " +
            "recipeId = :id")
    String getRecipeLevel(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RecipeType... recipeTypes);

    @Delete
    void delete(RecipeType recipeType);

    @Query("DELETE FROM recipeType")
    void deleteAll();
}
