package com.fanjavaid.github_explorer_android.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fanjavaid.github_explorer_android.data.model.Account

/**
 * Created by Fandi Akhmad (fanjavaid) on 23/08/20.
 */
@Database(entities = [Account::class], version = 1)
abstract class GithubExplorerDatabase : RoomDatabase() {
    abstract fun getAccountDao(): AccountDao

    companion object {
        @Volatile
        private var INSTANCE: GithubExplorerDatabase? = null

        fun getDatabase(context: Context): GithubExplorerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GithubExplorerDatabase::class.java,
                    "github_explorer_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
