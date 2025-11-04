package com.slack.exercise.model

import com.google.gson.annotations.SerializedName

/**
 * Models users returned by the API.
 */
data class UserSearchResult(val username: String, val displayName: String, val avatarUrl: String, val id: String)


