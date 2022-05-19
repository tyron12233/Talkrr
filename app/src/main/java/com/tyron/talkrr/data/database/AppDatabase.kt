package com.tyron.talkrr.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tyron.talkrr.data.database.converter.DateConverter
import com.tyron.talkrr.data.database.dao.UserDao
import com.tyron.talkrr.model.User

@Database(
    entities = [
//        Post::class,
        User::class
//        Comment::class,
//        Like::class
               ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        /**
         * Initialize [AppDatabase]
         * @param context the applicationContext
         * @param dbName the name of the database
         */
        fun init(context: Context, dbName: String = "Instant.db"): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, dbName).build()

    }
}