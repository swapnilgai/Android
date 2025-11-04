package com.slack.exercise.dataprovider

import com.slack.exercise.common.interactor.Interactor

interface DenyListDataProvider : Interactor {

    suspend fun isValidSearchQuery(searchTerm: String) : Boolean

    suspend fun getDenyList(): Set<String>

    suspend fun addSearchTermToDenylist(searchTerm: String)

    suspend fun setDenyList()
}