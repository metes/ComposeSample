package com.compose.network.requester

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException


suspend fun <T> APIResultStatus<T>.onSuccess(action: suspend (response: T?) -> Unit): APIResultStatus<T> {
    if (this is APIResultStatus.Success) {
        action(this.data.getOrNull())
    }
    return this
}

fun <T> APIResultStatus<T>.onSuccess(
    coroutineScope: CoroutineScope? = null,
    action:suspend (response: T?) -> Unit
): APIResultStatus<T> {
    if (this is APIResultStatus.Success) {
        coroutineScope?.launch {
            action(this@onSuccess.data.getOrNull())
        }
    }

    return this
}

fun <T> APIResultStatus<T>.onIdle(action: () -> Unit): APIResultStatus<T> {
    if (this is APIResultStatus.Idle) {
        action()
    }
    return this
}

fun <T> APIResultStatus<T>.onLoading(action: () -> Unit): APIResultStatus<T> {
    if (this is APIResultStatus.Idle) {
        action()
    }
    return this
}

fun <T> APIResultStatus<T>.onHTTPException(
    action: (exception: HttpException) -> Unit
): APIResultStatus<T> {
    if (this is APIResultStatus.HTTPException) {
        action(this.exception)
    }
    return this
}

fun <T> APIResultStatus<T>.onGeneralException(
    action: (exception: Exception) -> Unit
): APIResultStatus<T> {
    if (this is APIResultStatus.GeneralException) {
        action(this.exception)
    }
    return this
}
