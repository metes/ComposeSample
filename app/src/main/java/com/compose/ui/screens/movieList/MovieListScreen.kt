package com.compose.ui.screens.movieList

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.compose.BuildConfig
import com.compose.R
import com.compose.network.model.response.movie.popular.PopularMoviesResponse
import com.compose.ui.screens.MyRatingBar
import com.compose.ui.screens.MyToolbar
import com.compose.ui.screens.ShowIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MovieListScreen(viewModel: MovieListViewModel, context: Context) {
    ActionByUIState(context, viewModel)
}

@Composable
fun ActionByUIState(
    context: Context,
    viewModel: MovieListViewModel
) {
    when (val uiState = viewModel.movieListUiState.value) {
        is UiState.Idle -> {
            /* Do Nothing */
        }
        is UiState.Loading -> {
            Column {
                MyToolbar()
                ShowIndicator()
            }
        }
        is UiState.ListRefreshing,
        is UiState.MovieListScreenUiState -> {
            Column {
                MyToolbar()
                ShowMovieList(
                    content = viewModel.movieResult,
                    onRefresh = { viewModel.getMovieList(true) },
                    isRefreshing = uiState is UiState.ListRefreshing
                )
            }
        }
        is UiState.GeneralException -> {
            AlertDialog(
                onDismissRequest = { /* Do Nothing */ },
                title = { Text(context.getString(R.string.general_error)) },
                text = { Text(uiState.exception?.message.orEmpty()) }, // todo show meaningful message
                buttons = {
                    // todo add retry & exit buttons
                }
            )
        }
    }
}

@Composable
fun ShowMovieList(
    content: List<PopularMoviesResponse.Result>,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    MovieList(
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
        movieList = content ?: emptyList(),
    )
}

@Composable
fun MovieList(
    movieList: List<PopularMoviesResponse.Result>,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    SwipeRefresh(
        state =  rememberSwipeRefreshState(isRefreshing),
        onRefresh = { onRefresh() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            itemsIndexed(movieList) { _, item ->
                MovieListItem(item)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalCoilApi
@Composable
fun MovieListItem(item: PopularMoviesResponse.Result) {
    val cardBgColor = colorResource(id = R.color.material_blue_grey_50)
    val imageSize = "w300"
    val imageUrl = BuildConfig.BASE_URL_IMG + imageSize + item.posterPath
    Log.d("imageUrl-", imageUrl)
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.background(cardBgColor)
        ) {
            Image(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Color.Blue),
                contentScale = ContentScale.FillWidth,
                painter =
                    rememberImagePainter(imageUrl),
                contentDescription = item.title
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = item.title.orEmpty()
                )
                MyRatingBar(
                    modifier = Modifier.width(16.dp),
                    rating = item.voteAverage ?: 1.0
                )
                Text(
                    text = "${item.releaseDate}",
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}