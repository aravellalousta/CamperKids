package com.example.camperkids.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;

import java.util.List;

import com.example.camperkids.data.entities.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    User getUserById(int id);

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> users);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}