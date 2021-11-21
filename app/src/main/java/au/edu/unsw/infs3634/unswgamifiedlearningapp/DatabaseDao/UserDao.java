package au.edu.unsw.infs3634.unswgamifiedlearningapp.DatabaseDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.User;

@Dao
public interface UserDao {
    //Get all users
    @Query("SELECT * FROM user")
    List<User> getAll();

    //Get all users based on an array of IDs
    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    //Get a user by ID
    @Query("SELECT * FROM user WHERE " +
            "id LIKE :id LIMIT 1")
    User findById(String id);

    //Get the points of a user
    @Query("SELECT points FROM user WHERE " +
            "id LIKE :id LIMIT 1")
    Integer findPointsById(String id);

    //Update user points
    @Query("UPDATE user " +
            "SET points = :points " +
            "WHERE id LIKE :id")
    Integer updateUserPoints(String id, Integer points);

    //Get the users ordered by their points - FOR LEADERBOARD
    @Query("SELECT * " +
            "FROM user " +
            "ORDER BY points DESC ")
    List<User> getOrderedUsersByPoints();

    //Insert users
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... user);

    //Delete users
    @Delete
    void delete(User user);

    //Deletes all users
    @Query("DELETE FROM user")
    void delete();
}

