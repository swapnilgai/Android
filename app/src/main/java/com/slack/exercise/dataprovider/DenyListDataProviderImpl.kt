package com.slack.exercise.dataprovider

import android.content.Context
import com.slack.exercise.R
import com.slack.exercise.common.interactor.withInteractorContext
import com.slack.exercise.commondb.dao.DenylistDao
import com.slack.exercise.commondb.entity.Denylist
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [DenyListDataProvider] that loads denied search terms from raw resources.
 */
@Singleton
class DenyListDataProviderImpl @Inject constructor(
    private val context: Context,
    private val denylistDao: DenylistDao
) : DenyListDataProvider {

    override suspend fun isValidSearchQuery(searchTerm: String): Boolean {
        if (searchTerm.isBlank()) return false

        val denyList = getDenyList()
        val normalizedQuery = searchTerm.trim().lowercase()
        return !denyList.contains(normalizedQuery)
    }

    override suspend fun getDenyList(): Set<String> {
        return withInteractorContext() {

            val dynamicTerms = denylistDao.getAllTermsAsStrings().toSet()

           dynamicTerms
        }
    }

    override suspend fun setDenyList(){
        return withInteractorContext() {

            val result = loadDenyListFromRaw().map { it -> Denylist(term = it) }

            denylistDao.insertTerms(result)
        }
    }

    private fun loadDenyListFromRaw(): Set<String> {
        context.resources.openRawResource(R.raw.denylist).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8), 8192).use { reader ->
                val denySet = mutableSetOf<String>()

                reader.lineSequence()
                    .map { it.trim() }
                    .filter { line ->
                        line.isNotEmpty()
                    }
                    .forEach { line ->
                        denySet.add(line.lowercase())
                    }

                return denySet.toSet()
            }
        }
    }

    override suspend fun addSearchTermToDenylist(searchTerm: String) {
        if (searchTerm.isBlank()) return

        val normalizedTerm = searchTerm.trim().lowercase()

        withInteractorContext {
            denylistDao.insertTerm(Denylist(term = normalizedTerm))
        }
    }
}
