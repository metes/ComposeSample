package com.compose.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.compose.network.model.movie.MovieModel
import com.compose.network.requester.ApiRequester
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel(application: Application) : AndroidViewModel(application), KoinComponent {

    private val apiRequester by inject<ApiRequester>()

    private val _currencyList = MutableSharedFlow<ApiRequester.APIResult<MovieModel>>()
    val currencyListFlow = _currencyList.asSharedFlow()

    init {
        getCurrencyList()
    }

    fun getCurrencyList() {
        viewModelScope.launch {
            apiRequester.sendRequest(_currencyList) { client ->
                client.retrofitClient.getPopularMovies()
            }
        }
    }
}