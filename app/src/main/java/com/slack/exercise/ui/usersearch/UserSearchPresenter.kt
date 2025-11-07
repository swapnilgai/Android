package com.slack.exercise.ui.usersearch

import androidx.annotation.MainThread
import com.slack.exercise.commonui.component.UserSearchResult
import com.slack.exercise.dataprovider.DenyListDataProvider
import com.slack.exercise.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.ui.usersearch.model.UserSearchState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Presenter responsible for reacting to user inputs and initiating search queries.
 */
class UserSearchPresenterImpl @Inject constructor(
    private val userNameResultDataProvider: UserSearchResultDataProvider,
    private val denyListDataProvider: DenyListDataProvider
) : UserSearchPresenter {

    private val searchQuerySharedFlow =
        MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)

    private val stateFlow: MutableStateFlow<UserSearchState> =
        MutableStateFlow(UserSearchState(emptySet()))

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        setError(exception.localizedMessage ?: "An unexpected error occurred")
        //Centralize exception handling/logging here if needed (For splunk or crashlytics)
    }
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + coroutineExceptionHandler)

    override fun getUserSearchState(): StateFlow<UserSearchState> = stateFlow.asStateFlow()

    init {
        scope.launch {
            setLoading()
            async { denyListDataProvider.setDenyList() }
            searchQuerySharedFlow.distinctUntilChanged().debounce(300).map { searchTerm ->
                if (searchTerm.isEmpty()) {
                    Triple(emptySet<UserSearchResult>(), searchTerm, true)
                } else {
                    val validSearchTerm = denyListDataProvider.isValidSearchQuery(searchTerm)
                    val users = if(validSearchTerm) userNameResultDataProvider.fetchUsers(searchTerm) else emptySet<UserSearchResult>()
                    Triple(users, searchTerm, validSearchTerm)
                }
            }.collectLatest { (userList, searchTerm, validSearchTerm) ->
                setContent(userList)
                if(userList.isEmpty() && searchTerm.isNotEmpty())
                    async { denyListDataProvider.addSearchTermToDenylist(searchTerm) }
            }
        }
    }

    override fun detach() {
        scope.cancel()
    }

    override fun onQueryTextChange(searchTerm: String) {
        searchQuerySharedFlow.tryEmit(searchTerm)
    }

    @MainThread
    private fun setContent(userList: Set<UserSearchResult>){
        stateFlow.tryEmit(UserSearchState(userList = userList))
    }

    @MainThread
    private fun setError(msg: String){
        stateFlow.tryEmit(stateFlow.value.copy(
            isLoadingUi = false,
            error = msg
        ))
    }

    @MainThread
    private fun setLoading(){
        stateFlow.tryEmit(stateFlow.value.copy(isLoadingUi = true))
    }
}