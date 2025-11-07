package com.slack.exercise.common.cache

interface CacheExpirationPolicy {
    fun isEntryExpired(requestTimeStamp: Long): Boolean
}

data class ExpireAfterTimeout(
    val timeoutDurationMillis: Long,
    private val timeProvider: () -> Long = { System.currentTimeMillis() }
) : CacheExpirationPolicy {
    override fun isEntryExpired(requestTimeStamp: Long): Boolean {
        val now = timeProvider()
        return now >= requestTimeStamp + timeoutDurationMillis
    }
}

private const val DEFAULT_CACHE_EXPIRATION_DURATION = 1000L * 60 * 15 // 15 minutes
val defaultCacheExpiration = ExpireAfterTimeout(DEFAULT_CACHE_EXPIRATION_DURATION)


