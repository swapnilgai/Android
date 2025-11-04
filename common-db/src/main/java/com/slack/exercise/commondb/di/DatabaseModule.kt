package com.slack.exercise.commondb.di

import android.content.Context
import androidx.room.Room
import com.slack.exercise.commondb.SlackDatabase
import com.slack.exercise.commondb.dao.DenylistDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger module for providing database-related dependencies.
 */
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideSlackDatabase(context: Context): SlackDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SlackDatabase::class.java,
            "slack_database"
        ).build()
    }

    @Provides
    fun provideDenylistDao(database: SlackDatabase): DenylistDao {
        return database.denylistDao()
    }
}
