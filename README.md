# Slack User Search

A modular Android app for searching Slack users with caching and deny list filtering.

## Project Structure

**common** - Core utilities and shared business logic

**common-ui** - Reusable UI components for Compose

**cache** - In-memory LRU cache with expiration support

**interactor** - Business logic layer that handles threading, caching, and retries automatically

**common-db** - Local database using Room for persisting deny list

## Key Components

### DenyListDataProviderImpl

Manages blocked search terms with a smart data flow:

1. **Initial Load**: On first run, loads deny list from `R.raw.denylist` and stores it in the local database
2. **Subsequent Loads**: Reads deny list from the database (faster than reading raw files)
3. **Runtime Updates**: When searches return no results, new terms are added to the database
4. **In-Memory Cache**: Everything is cached in memory for quick lookups during searches

This approach gives you the best of both worlds - seed data from resources and persistent updates in the database.

### withInteractorContext

Extension function that handles the boring stuff - threading, caching, and retries.

**What it does:**
- Runs your code on a background thread
- Caches results automatically
- Retries failed operations

Example:
```kotlin
withInteractorContext(
    cacheOption = CacheOptions(key = "users_$query"),
    retryOption = RetryOption(retryCount = 3)
) {
    fetchUsersFromApi(query)
}
```

## Why This Setup?

Modular design keeps things organized. Each module does one thing well. Easy to test, easy to reuse, and performs well with built-in caching.

## Tech Stack

Kotlin, Jetpack Compose, Coroutines, Dagger, Room, Coil, Retrofit

## UI Implementation

The entire UI is built with Jetpack Compose. No XML layouts were added - everything is pure Compose code.


