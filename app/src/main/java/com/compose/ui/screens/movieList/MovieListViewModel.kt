package com.compose.ui.screens.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.popular.PopularMoviesResponse
import com.compose.network.requester.APIResultStatus
import com.compose.network.requester.onGeneralException
import com.compose.network.requester.onSuccess
import com.compose.tools.toMovieEntity
import com.compose.ui.screens.movieList.useCase.FetchMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

enum class ListType {
    Popular, TopRated, Upcoming
}

class MovieListViewModel : ViewModel(), KoinComponent {

    private val moviesUseCase by inject<FetchMoviesUseCase>()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    var pagingData: Flow<PagingData<MovieEntity>>? = null

    init {
        getMovieList(ListType.Popular)
    }

    fun getMovieList(listType: ListType) {
        viewModelScope.launch(Dispatchers.IO) {
            setState(UiState.ListRefreshing(false))

            pagingData?.let {
                setState(UiState.MovieListScreenUiState(movieList = it))
            }
        }

        val movieSource = MoviePagingSource(::handleNotSuccessResults, moviesUseCase, listType)

        pagingData = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { movieSource }
        ).flow
    }

    private fun handleNotSuccessResults(
        apiResultStatus: APIResultStatus<PopularMoviesResponse>
    ) {
        viewModelScope.launch {
            when (apiResultStatus) {
                is APIResultStatus.Idle -> _uiState.emit(UiState.Idle)
                is APIResultStatus.Loading -> {
                }
                is APIResultStatus.GeneralException -> {
                    _uiState.emit(UiState.GeneralException(apiResultStatus.exception))
                }
                else -> {
                    /** Dont anything */
                }
            }
        }
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            moviesUseCase.getMovieDetails(movieId) { apiResultStatus ->
                apiResultStatus.onSuccess {
                    it?.let { safeDetailResponse ->
                        setState(
                            UiState.DetailDialog(
                                safeDetailResponse.toMovieEntity(),
                                pagingData?: flowOf()
                            ),
                        )
                    } ?: run {
                        setState(UiState.GeneralException())
                    }
                }.onGeneralException {
                    setState(UiState.GeneralException())
                }
            }
        }
    }

    fun setState(state: UiState) {
        viewModelScope.launch {
            _uiState.emit(state)
        }
    }

}
