package com.compose.ui.screens.movieList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.compose.db.entity.MovieEntity
import com.compose.ui.screens.movieList.useCase.FetchMoviesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MoviePagingSource(
    private val listType: ListType
) : PagingSource<Int, MovieEntity>(), KoinComponent {

    private val fetchMoviesUseCase: FetchMoviesUseCase by inject()

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MovieEntity> {
        val pageIndex = (params.key ?: STARTING_INDEX)

        return try {
            LoadResult.Page(
                data = fetchMoviesUseCase.fetchMovies(listType, pageIndex),
                prevKey = null, // Only paging forward.
                nextKey = pageIndex + 1
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            e.printStackTrace()

            LoadResult.Error(e)
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
    }

}