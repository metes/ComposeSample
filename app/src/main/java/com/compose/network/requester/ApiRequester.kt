package com.compose.network.requester

import com.compose.network.client.APIClient
import com.compose.network.restClient.RestClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApiRequester: KoinComponent {

    private val client by inject<APIClient>()

    /**
     * @param requestFunc Suspend function used for API send request.
     */
    suspend fun <T>sendRequest(requestFunc: suspend RestClient.() -> T) : APIResultStatus<T> {
        return try {
            APIResultStatus.Success(Result.success(requestFunc(client.retrofitClient)))
        } catch (e: Exception) {
            e.printStackTrace()
            APIResultStatus.GeneralException(e)
        }
    }
}

/**
 *
 */
sealed class APIResultStatus<T> {
    class Idle<T> : APIResultStatus<T>()
    class Loading<T> : APIResultStatus<T>()
    data class GeneralException<T>(val exception: Exception) : APIResultStatus<T>()
    data class Success<T>(val data: Result<T>) : APIResultStatus<T>()
}
