package com.slack.exercise.commonui

import androidx.annotation.MainThread
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Represents text that can be displayed in the UI
 */
@Stable
sealed interface UiText {
    data class StringResource(val resId: Int) : UiText
    data class DynamicString(val value: String) : UiText
}

@Stable
data class UiStateHolder<T>(
    val data: T,
    val isLoadingUi: Boolean = true,
    val error: UiText? = null
)

abstract class BasePresenter<T>(initialContent: T) {
    // Use SupervisorJob for resilience - if one coroutine fails, others continue
    protected val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _contentState: MutableStateFlow<UiStateHolder<T>> =
        MutableStateFlow(UiStateHolder(data = initialContent))

    val state = _contentState.asStateFlow()

    @MainThread
    protected fun setContent(content: T){
        _contentState.update { UiStateHolder(data = content, isLoadingUi = false, error = null) }
    }

    @MainThread
    protected fun setError(message: UiText) {
        _contentState.update { it.copy(error = message) }
    }

    @MainThread
    protected fun setLoading(){
        _contentState.update { it.copy(isLoadingUi = true) }
    }

}