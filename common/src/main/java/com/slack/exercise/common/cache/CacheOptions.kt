package com.slack.exercise.common.cache

data class CacheOptions(
    val key: CacheKey,
    val expirationPolicy: CacheExpirationPolicy = defaultCacheExpiration,
    val allowOverwrite: Boolean = true,
)
