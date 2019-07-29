package saedc.example.com.Model.Dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import saedc.example.com.Model.Entity.User;

@Dao
public interface UserDao {
    @Query("SELECT COUNT(*) FROM user")
    Integer getUsersCounts();

    @Query("SELECT * FROM USER WHERE ID=:id ")
    LiveData<User> getUserById(int id);



    @Query("DELETE FROM USER WHERE id = :id")
    void deleteUser(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User s);


}
