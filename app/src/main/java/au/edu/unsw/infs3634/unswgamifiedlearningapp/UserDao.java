package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE " +
            "id LIKE :id LIMIT 1")
    User findById(String id);

    @Query("SELECT points FROM user WHERE " +
            "id LIKE :id LIMIT 1")
    Integer findPointsById(String id);

    @Query("UPDATE user " +
            "SET points = :points " +
            "WHERE id LIKE :id")
    Integer updateUserPoints(String id, Integer points);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... user);

    @Delete
    void delete(User user);

    //Deletes all users
    @Query("DELETE FROM user")
    void delete();
}

