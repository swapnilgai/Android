package com.slack.exercise.commondb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "denylist")
data class Denylist(
    @PrimaryKey val term: String,
    val addedTimestamp: Long = System.currentTimeMillis()
)
