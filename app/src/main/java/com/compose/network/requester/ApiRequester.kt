package com.compose.network.requester

import com.compose.network.client.APIClient
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException

class ApiRequester: KoinComponent {

    private val client by inject<APIClient>()

    suspend fun <T>sendRequest(
        sharedFlow: MutableSharedFlow<APIResult<T>>,
        requestFunc: suspend (apiClient: APIClient) -> T
    ) {
        sharedFlow.emit(APIResult.Loading())
        try {
            sharedFlow.emit(APIResult.Success(Result.success(requestFunc(client))))
        } catch (httpException: HttpException) {
            httpException.printStackTrace()
            sharedFlow.emit(APIResult.HTTPException(httpException))
        } catch (e: Exception) {
            e.printStackTrace()
            sharedFlow.emit(APIResult.GeneralException(e))
        }
    }

    sealed class APIResult<T> {
        class Idle<T> : APIResult<T>()
        class Loading<T> : APIResult<T>()
        data class HTTPException<T>(val exception: HttpException) : APIResult<T>()
        data class GeneralException<T>(val exception: Exception) : APIResult<T>()
        data class Success<T>(val data: Result<T>) : APIResult<T>()
    }

}