package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, UserFavouriteRecipe.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract UserFavouriteRecipeDao userFavouriteRecipeDao();
}
