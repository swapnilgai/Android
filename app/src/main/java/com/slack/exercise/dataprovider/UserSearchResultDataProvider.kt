package com.slack.exercise.dataprovider

import com.slack.exercise.common.interactor.Interactor
import com.slack.exercise.model.UserSearchResult

/**
 * Provider of [UserSearchResult].
 * This interface abstracts the logic of searching for users through the API or other data sources.
 */
interface UserSearchResultDataProvider : Interactor {

  /**
   * Returns a set of [UserSearchResult].
   * Added forceRefresh for pull to refresh capability.
   */
  suspend fun fetchUsers(searchTerm: String, forceRefresh: Boolean = false): Set<UserSearchResult>
}