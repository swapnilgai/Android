package com.slack.exercise.dataprovider

import com.slack.exercise.api.SlackApi
import com.slack.exercise.common.cache.CacheKey
import com.slack.exercise.common.cache.CacheOptions
import com.slack.exercise.common.interactor.RetryOption
import com.slack.exercise.common.interactor.withInteractorContext
import com.slack.exercise.model.UserSearchResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [UserSearchResultDataProvider].
 */
private data class SearchCacheKey(val searchTerm: String): CacheKey
@Singleton
class UserSearchResultDataProviderImpl @Inject constructor(
    private val slackApi: SlackApi
) : UserSearchResultDataProvider {

    /**
     * Returns a set of [UserSearchResult].
     */
    override suspend fun fetchUsers(searchTerm: String, forceRefresh: Boolean): Set<UserSearchResult>
    = withInteractorContext(cacheOption = CacheOptions(key =  SearchCacheKey(searchTerm = searchTerm)),
        retryOption = RetryOption(1)) {
        slackApi.searchUsers(searchTerm)
            .map { user ->
                UserSearchResult(user.username)
            }.toSet()
    }
}