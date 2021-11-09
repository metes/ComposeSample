package com.compose.ui.screen.movieList

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.compose.R
import com.compose.network.model.movie.MovieModel
import com.compose.network.model.movie.MovieResult
import com.compose.network.requester.ApiRequester
import com.compose.ui.screen.MyToolbar
import com.compose.ui.screen.ShowIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun MovieListScreen(repository: MovieListRepo) {
    val scope = rememberCoroutineScope()

    val currencyState by repository.currencyListFlow.collectAsState(
        initial = ApiRequester.APIResult.Idle()
    )

    when (currencyState) {
        is ApiRequester.APIResult.Loading<*> -> {
            ShowIndicator()
        }
        is ApiRequester.APIResult.Success<*> -> {
            val successModel = (currencyState as ApiRequester.APIResult.Success<*>)
            ShowMovieList(
                content = successModel.data.getOrNull() as MovieModel?,
                onRefresh = { repository.getCurrencyList(scope) },
                moviesFlow = repository.currencyListFlow
            )
        }
        is ApiRequester.APIResult.HTTPException -> {
            AlertDialog(
                onDismissRequest = { /*TODO*/ },
                title = { Text("http exception") },
                text = { Text("ttt") },
                buttons = {

                }
            )
        }
        is ApiRequester.APIResult.GeneralException -> {
            AlertDialog(
                onDismissRequest = { /*TODO*/ },
                title = { Text("general exception") },
                text = { Text("ttt") },
                buttons = { }
            )
        }
    }

    MakeInitialRequest(repository, scope)
}

@Composable
fun MakeInitialRequest(repository: MovieListRepo, scope: CoroutineScope) {
    LaunchedEffect(repository.currencyListFlow.replayCache.isEmpty()) {
        repository.getCurrencyList(scope)
    }
}

@Composable
fun ShowMovieList(
    content: MovieModel?,
    onRefresh: () -> Unit,
    moviesFlow: SharedFlow<ApiRequester.APIResult<MovieModel>>
) {
    Column {
        MyToolbar()
        MovieList(content?.movieResults ?: emptyList(), onRefresh, moviesFlow)
    }
}

@Composable
fun MovieList(
    movieList: List<MovieResult>,
    onRefresh: () -> Unit,
    moviesFlow: SharedFlow<ApiRequester.APIResult<MovieModel>>
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(moviesFlow is ApiRequester.APIResult.Loading<*>),
        onRefresh = { onRefresh() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            itemsIndexed(movieList) { _, item -> MovieItem(item) }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieItem(item: MovieResult) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.background(Color.LightGray)
        ) {
            Image(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Color.Blue),
                painter = painterResource(R.drawable.ic_imdb_logo_2016),
                contentDescription = "Contact profile picture",
            )
            Column(
                modifier = Modifier
                    //.height(64.dp)
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = item.title)
                Text(text = "${item.year}")
            }
        }
    }
}