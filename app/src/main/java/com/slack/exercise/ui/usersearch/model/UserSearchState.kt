package com.slack.exercise.ui.usersearch.model

import com.slack.exercise.commonui.UiText
import com.slack.exercise.model.UserSearchResult

/**
 * Data class that represents the UI state of the UserSearchFragment.
 * This class contains the list of user names to be displayed.
 */
data class UserSearchState(val userList: Set<UserSearchResult>, val isLoadingUi: Boolean = false, val error: UiText?=null)