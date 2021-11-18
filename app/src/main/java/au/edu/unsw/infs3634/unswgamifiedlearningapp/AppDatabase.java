package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

//All the database entities used in the app
@Database(entities = {User.class, UserFavouriteRecipe.class, RecipeInformationResult.class,
            RecipeType.class, UserRecipeCompleted.class}, version = 5)
//Converts custom types in the database/classes
@TypeConverters({ObjectConverter.class, StringConverter.class, IngredientsConverter.class})

public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract UserFavouriteRecipeDao userFavouriteRecipeDao();
    public abstract RecipesDao recipesDao();
    public abstract RecipeTypeDao recipeTypeDao();
    public abstract UserRecipeCompletedDao userRecipeCompletedDao();
}
