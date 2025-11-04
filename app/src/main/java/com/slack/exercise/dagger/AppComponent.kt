package com.slack.exercise.dagger

import android.content.Context
import com.slack.exercise.commondb.di.DatabaseModule
import com.slack.exercise.dataprovider.DenyListDataProvider
import com.slack.exercise.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.ui.dagger.BindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

/**
 * Component providing Application scoped instances.
 */
@Singleton
@Component(modules = [
  AppModule::class,
  DatabaseModule::class,
  AndroidInjectionModule::class,
  BindingModule::class
])
interface AppComponent : AndroidInjector<DaggerApplication> {
  fun userSearchResultDataProvider(): UserSearchResultDataProvider
  fun denyListDataProvider(): DenyListDataProvider

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun context(context: Context): Builder
    fun build(): AppComponent
  }
}