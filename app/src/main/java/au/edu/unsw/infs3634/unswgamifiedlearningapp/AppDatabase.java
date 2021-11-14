package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {User.class, UserFavouriteRecipe.class, RecipeInformationResult.class,
            RecipeType.class}, version = 3)
@TypeConverters({ObjectConverter.class, StringConverter.class, IngredientsConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract UserFavouriteRecipeDao userFavouriteRecipeDao();
    public abstract RecipesDao recipesDao();
    public abstract RecipeTypeDao recipeTypeDao();
}
