package com.slack.exercise.commondb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.slack.exercise.commondb.dao.DenylistDao
import com.slack.exercise.commondb.entity.Denylist

/**
 * Room database for the Slack exercise app.
 *
 * Contains all database entities and provides access to DAOs.
 */
@Database(
    entities = [Denylist::class],
    version = 1,
    exportSchema = false
)
abstract class SlackDatabase : RoomDatabase() {
    abstract fun denylistDao(): DenylistDao
}
