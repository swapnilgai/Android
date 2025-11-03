package com.slack.exercise.common.cache

import androidx.collection.LruCache

interface CacheKey

data class StringCacheKey(val str: String) : CacheKey


private data class CacheEntry(
    val data: Any,
    val requestTimeStamp: Long,
    val expirationPolicy: CacheExpirationPolicy
)

class Cache internal constructor(maxSize: Int){
    private val lruCache: LruCache<CacheKey, CacheEntry> = LruCache(maxSize)

    @Synchronized
    fun set(key: CacheKey, value: Any?) {

        if(value == null) {
            lruCache.remove(key)
            return
        }

        lruCache.put(key, CacheEntry(
                data = value,
                requestTimeStamp = System.currentTimeMillis(),
                expirationPolicy = defaultCacheExpiration
            )
        )
    }

    @Synchronized
    fun <T> get(key: CacheKey): T? {
        return lruCache[key]?.let { entry ->
            if (entry.expirationPolicy.isEntryExpired(entry.requestTimeStamp)) {
                lruCache.remove(key)
                null
            } else {
                entry.data as T
            }
        }
    }

    @Synchronized
    fun remove(key: CacheKey) {
        lruCache.remove(key)
    }

    @Synchronized
    fun clear() {
        lruCache.evictAll()
    }

}


