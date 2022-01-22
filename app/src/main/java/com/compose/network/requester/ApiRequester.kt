package com.compose.network.requester

import com.compose.network.client.APIClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException

class ApiRequester: KoinComponent {

    private val client by inject<APIClient>()

    /**
     * @param function Suspend function used for receiving API response.
     * @param requestFunc Suspend function used for API send request.
     */
    suspend fun <T>sendRequest(
        requestFunc: suspend APIClient.() -> T,
        function: suspend (apiResultStatus: APIResultStatus<T>) -> Unit
    ) {
        function(APIResultStatus.Loading())
        try {
            function(APIResultStatus.Success(Result.success(requestFunc(client))))
        } catch (httpException: HttpException) {
            httpException.printStackTrace()
            function(APIResultStatus.HTTPException(httpException))
        } catch (e: Exception) {
            e.printStackTrace()
            function(APIResultStatus.GeneralException(e))
        }
    }
}

/**
 *
 */
sealed class APIResultStatus<T> {
    class Idle<T> : APIResultStatus<T>()
    class Loading<T> : APIResultStatus<T>()
    data class HTTPException<T>(val exception: HttpException) : APIResultStatus<T>()
    data class GeneralException<T>(val exception: Exception) : APIResultStatus<T>()
    data class Success<T>(val data: Result<T>) : APIResultStatus<T>()
}
