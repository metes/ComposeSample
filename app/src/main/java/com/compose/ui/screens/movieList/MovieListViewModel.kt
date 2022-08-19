package com.compose.ui.screens.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.compose.db.entity.MovieEntity
import com.compose.tools.toMovieEntity
import com.compose.ui.screens.movieList.uistate.MovieScreenUiState
import com.compose.ui.screens.movieList.useCase.FetchMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MovieListViewModel : ViewModel(), KoinComponent {

    private val fetchMoviesUseCase: FetchMoviesUseCase by inject()

    private val _movieListState =
        MutableStateFlow<MovieScreenUiState.MovieList>(MovieScreenUiState.MovieList.Idle)
    val movieListState = _movieListState.asStateFlow()

    private val _dialogUiState =
        MutableStateFlow<MovieScreenUiState.DetailDialog>(MovieScreenUiState.DetailDialog.Idle)
    val dialogUiState = _dialogUiState.asStateFlow()

    private var currentListType: ListType = ListType.Popular
    val currentPagingData: Flow<PagingData<MovieEntity>>?
        get() = pagingData[currentListType]

    private var pagingData: HashMap<ListType, Flow<PagingData<MovieEntity>>> = hashMapOf()

    init {
        getMovieList(ListType.Popular)
    }

    fun getMovieList(listType: ListType) {
        currentListType = listType

        if (pagingData[listType] == null) {
            pagingData[listType] = flowOf()
        }

        pagingData[listType] = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(listType) }
        ).flow

        viewModelScope.launch {
            _movieListState.emit(
                MovieScreenUiState.MovieList.MovieListScreenMovieList(currentListType = listType)
            )
        }
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            if (movieId <= 0) {
                _dialogUiState.emit(MovieScreenUiState.DetailDialog.Idle)
            } else {
                fetchMoviesUseCase.getMovieDetails(movieId) { movieDetailResponse ->
                    movieDetailResponse?.let {
                        _dialogUiState.emit(
                            MovieScreenUiState.DetailDialog.DetailDialog(it.toMovieEntity())
                        )
                    }?: run {
                        _dialogUiState.emit(MovieScreenUiState.DetailDialog.Idle)
                    }
                }
            }
        }
    }
}
