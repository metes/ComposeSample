package com.compose.network.requester

import com.compose.network.client.APIClient
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException

class ApiRequester: KoinComponent {

    private val client by inject<APIClient>()

    /**
     * @param sharedFlow Where current request status is kept.
     * @param requestFunc Suspend function used for API send request.
     */
    suspend fun <T>sendRequest(
        sharedFlow: MutableSharedFlow<APIResultStatus<T>>,
        requestFunc: suspend (apiClient: APIClient) -> T
    ) {
        sharedFlow.emit(APIResultStatus.Loading())
        try {
            sharedFlow.emit(APIResultStatus.Success(Result.success(requestFunc(client))))
        } catch (httpException: HttpException) {
            httpException.printStackTrace()
            sharedFlow.emit(APIResultStatus.HTTPException(httpException))
        } catch (e: Exception) {
            e.printStackTrace()
            sharedFlow.emit(APIResultStatus.GeneralException(e))
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