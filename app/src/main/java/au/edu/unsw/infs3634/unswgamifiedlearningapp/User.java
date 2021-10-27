package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "user")
public class User {
    @ColumnInfo(name = "id")

    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "password")
    public String password;

    //Constructor
    public User(String id, String firstName, String lastName, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    // Insert Dummy Data into the Database
    public static User[] userData(){
        return new User[]{
                new User("testUser", "First Name", "Last Name", "password"),
                new User("joyaj", "Joya", "Jeong", "jj")
        };
    }

    //Create getter & setter methods
    public void setId(String id) {
        this.id = id;
    }
}