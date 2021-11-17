package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipesDao {
    //Get all recipes
    @Query("SELECT * FROM recipes")
    List<RecipeInformationResult> getAll();

    //Get recipe by ID
    @Query("SELECT * FROM recipes WHERE " +
            "id = :id LIMIT 1")
    RecipeInformationResult findById(Integer id);

    //Inserts recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RecipeInformationResult... recipes);

    //Delete recipe
    @Delete
    void delete(RecipeInformationResult recipe);

    //Deletes all recipes
    @Query("DELETE FROM recipes")
    void deleteAll();

}
