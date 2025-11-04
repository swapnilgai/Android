package com.slack.exercise.commondb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.slack.exercise.commondb.entity.Denylist

/**
 * Data Access Object for denylist terms.
 *
 * Provides database operations for managing dynamic denylist terms.
 */
@Dao
interface DenylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTerm(term: Denylist)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTerms(terms: List<Denylist>)

    @Query("SELECT * FROM denylist")
    suspend fun getAllTerms(): List<Denylist>

    @Query("SELECT EXISTS(SELECT 1 FROM denylist WHERE term = :term)")
    suspend fun doesTermExist(term: String): Boolean

    @Query("SELECT term FROM denylist")
    suspend fun getAllTermsAsStrings(): List<String>

    @Query("DELETE FROM denylist WHERE term = :term")
    suspend fun deleteTerm(term: String)

    @Query("DELETE FROM denylist")
    suspend fun clearAllTerms()
}
