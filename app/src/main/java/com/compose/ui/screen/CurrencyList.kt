package com.compose.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.compose.network.model.movie.MovieModel
import com.compose.network.model.movie.MovieResult
import com.compose.network.requester.ApiRequester
import com.compose.ui.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun MovieListScreen(viewModel: MainViewModel) {

    val currencyState by viewModel.currencyListFlow.collectAsState(
        initial = ApiRequester.APIResult.Loading()
    )

    when (currencyState) {
        is ApiRequester.APIResult.Loading<*> -> {
            ShowIndicator()
        }
        is ApiRequester.APIResult.Success<*> -> {
            val successModel = (currencyState as ApiRequester.APIResult.Success<*>)
            AddLazyColumn(
                rows = successModel.data.getOrNull() as MovieModel?,
                onRefresh = { viewModel.getCurrencyList() },
                moviesFlow = viewModel.currencyListFlow
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
}


@Composable
fun ShowIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun AddLazyColumn(
    rows: MovieModel?,
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
            items(rows?.movieResults ?: emptyList()) { item -> AddItem(item) }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AddItem(item: MovieResult) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)
    ) {
        /*
        Image(
            //painter = painterResource(R.drawable.ae),
            contentDescription = "content description",
            modifier = Modifier.size(24.dp)
        )
        */
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp),
            text = item.title
        )
    }
}
