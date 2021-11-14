package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipesDao {
    @Query("SELECT * FROM recipes")
    List<RecipeInformationResult> getAll();

    @Query("SELECT * FROM recipes WHERE " +
            "id = :id LIMIT 1")
    RecipeInformationResult findById(Integer id);

    //Get all vegetarian recipes
    @Query("SELECT * FROM recipes WHERE " +
            "vegetarian = 1 AND " +
            "readyInMinutes <= :level"
    )
    List<RecipeInformationResult> findVegRecipes(int level);

    //Get all gluten free recipes
    @Query("SELECT * FROM recipes WHERE " +
            "glutenFree = 1 AND " +
            "readyInMinutes <= :level"
    )
    List<RecipeInformationResult> findGlutenFRecipes(int level);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RecipeInformationResult... recipes);

    @Delete
    void delete(RecipeInformationResult recipe);

    //Deletes all recipes
    @Query("DELETE FROM recipes")
    void deleteAll();

}
