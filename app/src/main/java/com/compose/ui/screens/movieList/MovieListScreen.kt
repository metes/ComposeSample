package com.compose.ui.screens.movieList

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.compose.R
import com.compose.db.entity.MovieEntity
import com.compose.ui.screens.MyRatingBar
import com.compose.ui.screens.MyToolbar
import com.compose.ui.screens.getImageFromMovieEntity
import com.compose.ui.screens.getString
import com.compose.ui.screens.movieList.uistate.MovieScreenUiState

object MovieListScreen {

    @OptIn(ExperimentalCoilApi::class)
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun SetScreen(viewModel: MovieListViewModel) {
        ActionByUIState(viewModel)
    }

    @ExperimentalCoilApi
    @Composable
    private fun ActionByUIState(viewModel: MovieListViewModel) {
        val movieUiState by viewModel.movieListState.collectAsState()
        val dialogUiState by viewModel.dialogUiState.collectAsState()

        Column {
            MyToolbar()
            DropDownList(viewModel)

            HandleMovieListState(movieUiState, viewModel)

            HandleDetailDialogState(dialogUiState, viewModel)
        }
    }

    @Composable
    private fun HandleDetailDialogState(
        collectedDetailDialogUiState: MovieScreenUiState.DetailDialog,
        viewModel: MovieListViewModel
    ) {
        when (collectedDetailDialogUiState) {
            is MovieScreenUiState.DetailDialog.Idle -> Unit
            is MovieScreenUiState.DetailDialog.DetailDialog -> {
                MovieDetailsDialog.SetView(movieEntity = collectedDetailDialogUiState.movie) {
                    viewModel.getMovieDetails(0)
                }
            }
        }
    }

    @Composable
    private fun HandleMovieListState(
        collectedMovieListState: MovieScreenUiState.MovieList,
        viewModel: MovieListViewModel
    ) {
        when (collectedMovieListState) {
            is MovieScreenUiState.MovieList.Idle -> Unit
            is MovieScreenUiState.MovieList.Loading -> Unit
            is MovieScreenUiState.MovieList.ListRefreshing -> Unit
            is MovieScreenUiState.MovieList.MovieListScreenMovieList -> {
                ShowMovieList(
                    viewModel = viewModel,
//                    onRefresh = { viewModel.getMovieList(ListType.Popular) },
//                    isRefreshing = uiState is UiState.ListRefreshing,
                )
            }
            is MovieScreenUiState.MovieList.GeneralException -> {
                ExceptionAlert(viewModel, collectedMovieListState)
            }
        }
    }

    @ExperimentalCoilApi
    @Composable
    private fun ShowMovieList(viewModel: MovieListViewModel) {
        val lazyItems = viewModel.currentPagingData?.collectAsLazyPagingItems() ?: return

        //    SwipeRefresh(
//        state = rememberSwipeRefreshState(isRefreshing),
//        onRefresh = { onRefresh() }
//    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(lazyItems.itemCount) { index ->
                lazyItems[index]?.let { movieEntity ->
                    MovieListItem(movieEntity, index + 1, viewModel)
                }
            }
        }
//    }
    }

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
    @ExperimentalCoilApi
    @Composable
    private fun MovieListItem(
        movieEntity: MovieEntity,
        index: Int,
        viewModel: MovieListViewModel
    ) {
        val cardBgColor = colorResource(id = R.color.material_blue_grey_50)

        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = 4.dp,
            onClick = {
                viewModel.getMovieDetails(movieEntity.id)
            },
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .clickable {
                    viewModel.getMovieDetails(movieEntity.id)
                }
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
                    painter = rememberImagePainter(getImageFromMovieEntity(movieEntity = movieEntity)),
                    contentDescription = movieEntity.title
                )
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "$index - ${movieEntity.title}"
                    )

                    MyRatingBar(
                        modifier = Modifier.width(16.dp),
                        rating = movieEntity.voteAverage.toDouble()
                    )

                    Text(
                        text = movieEntity.releaseDate,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }

    @Composable
    private fun DropDownList(viewModel: MovieListViewModel) {
        var expanded by remember { mutableStateOf(false) }
        val listTypeNames = ListType.values().map { it.name }
        var selectedText by remember { mutableStateOf(listTypeNames.first()) }

        var dropDownWidth by remember { mutableStateOf(0) }

        val icon = if (expanded) {
            Icons.Filled.ArrowForward
        } else {
            Icons.Filled.ArrowDropDown
        }

        Column(
            modifier = Modifier.clickable {
                expanded = true
            }
        ) {
            OutlinedTextField(
                enabled = false,
                value = selectedText,
                onValueChange = { selectedText = it },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .onSizeChanged { dropDownWidth = it.width },
                label = { ListType.valueOf(selectedText) },
                trailingIcon = {
                    Icon(icon, "contentDescription", Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(LocalDensity.current) { dropDownWidth.toDp() })
            ) {
                listTypeNames.forEach { label ->
                    DropdownMenuItem(
                        onClick = {
                            selectedText = label
                            viewModel.getMovieList(ListType.valueOf(label))
                            expanded = false
                        }
                    ) {
                        Text(text = label)
                    }
                }
            }
        }
    }

    @Composable
    private fun ExceptionAlert(
        viewModel: MovieListViewModel,
        movieListState: MovieScreenUiState.MovieList.GeneralException
    ) {
        AlertDialog(
            onDismissRequest = { /* Do Nothing */ },
            title = { Text(getString(R.string.general_error)) },
            text = { Text(movieListState.exception?.message.orEmpty()) }, // todo show meaningful message
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = { viewModel.getMovieList(ListType.Popular) }
                    ) {
                        Text(getString(R.string._cancel))
                    }
                }
            }
        )
    }
}