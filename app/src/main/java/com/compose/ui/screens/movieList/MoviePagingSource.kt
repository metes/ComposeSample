package com.compose.ui.screens.movieList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.popular.PopularMoviesResponse
import com.compose.network.requester.APIResultStatus
import com.compose.ui.screens.movieList.useCase.FetchMoviesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

class MoviePagingSource(
    val resultStatus: (APIResultStatus<PopularMoviesResponse>) -> Unit,
    val useCase: FetchMoviesUseCase,
    val listType: ListType
): PagingSource<Int, MovieEntity>() {

    var nextPageNumber = 1

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MovieEntity> {
        val position = (params.key ?: STARTING_INDEX)
        var loadResult: LoadResult<Int, MovieEntity>

        try {
            val response = useCase.fetchMovieListByType(
                resultStatus = resultStatus,
                listType = listType,
                page = position
            )

            return LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward.
                nextKey =  position + SEARCH_RESULTS_PAGE_SIZE
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            e.printStackTrace()

            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val STARTING_INDEX = 1
        const val SEARCH_RESULTS_PAGE_SIZE = 10
    }

}