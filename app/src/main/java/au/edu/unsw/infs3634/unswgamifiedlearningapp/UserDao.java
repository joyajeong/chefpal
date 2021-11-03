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

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void delete();
}

