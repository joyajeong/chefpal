package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.RecipeTypeDao;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.RecipesDao;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.UserDao;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.UserFavouriteRecipeDao;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao.UserRecipeCompletedDao;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.RecipeAPIConverters.IngredientsConverter;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.RecipeAPIConverters.ObjectConverter;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.RecipeAPIConverters.StringConverter;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.RecipeAPIEntities.RecipeInformationResult;

//All the database entities used in the app
@Database(entities = {User.class, UserFavouriteRecipe.class, RecipeInformationResult.class,
            RecipeType.class, UserRecipeCompleted.class}, version = 3)
//Converts custom types in the database/classes
@TypeConverters({ObjectConverter.class, StringConverter.class, IngredientsConverter.class})

public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract UserFavouriteRecipeDao userFavouriteRecipeDao();
    public abstract RecipesDao recipesDao();
    public abstract RecipeTypeDao recipeTypeDao();
    public abstract UserRecipeCompletedDao userRecipeCompletedDao();
}
