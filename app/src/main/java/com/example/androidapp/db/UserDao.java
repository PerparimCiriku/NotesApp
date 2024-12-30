package com.example.androidapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidapp.models.User;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Query("SELECT id FROM User WHERE username = :username LIMIT 1")
    int getUserId(String username);

    @Query("SELECT * from User WHERE username = :username and passwordHash = :password")
    User validateUser(String username, String password);
}