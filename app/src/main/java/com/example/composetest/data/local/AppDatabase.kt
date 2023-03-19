package com.example.composetest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composetest.model.User
import com.example.composetest.model.local.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
}