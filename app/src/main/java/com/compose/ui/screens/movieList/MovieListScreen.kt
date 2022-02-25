package com.compose.ui.screens.movieList

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.compose.BuildConfig
import com.compose.R
import com.compose.db.entity.MovieEntity
import com.compose.ui.screens.CustomDialog
import com.compose.ui.screens.MyRatingBar
import com.compose.ui.screens.MyToolbar
import com.compose.ui.screens.ShowIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@SuppressLint("UnrememberedMutableState")
@Composable
fun MovieListScreen(viewModel: MovieListViewModel, context: Context) {
    ActionByUIState(context, viewModel)
}

@ExperimentalCoilApi
@Composable
fun ActionByUIState(
    context: Context,
    viewModel: MovieListViewModel
) {
    val collectedUIState by viewModel.uiState.collectAsState()

    Column {
        MyToolbar()
        DropDownList(viewModel)

        when (val uiState = collectedUIState) {
            is UiState.Idle -> {
                /* Do Nothing */
            }
            is UiState.Loading -> {
                ShowIndicator()
            }
            is UiState.ListRefreshing,
            is UiState.MovieListScreenUiState -> {
                val movieList = if (uiState is UiState.MovieListScreenUiState) {
                    uiState.movieList
                } else {
                    emptyList()
                }
                ShowMovieList(
                    movieList = movieList,
                    onRefresh = { viewModel.getMovieList(true) },
                    isRefreshing = uiState is UiState.ListRefreshing
                )
            }
            is UiState.GeneralException -> {
                AlertDialog(
                    onDismissRequest = { /* Do Nothing */ },
                    title = { Text(context.getString(R.string.general_error)) },
                    text = { Text(uiState.exception?.message.orEmpty()) }, // todo show meaningful message
                    buttons = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                modifier = Modifier.padding(8.dp),
                                onClick = { viewModel.getMovieList() }
                            ) {
                                Text(context.getString(R.string._cancel))
                            }
                        }
                    }
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ShowMovieList(
    movieList: List<MovieEntity>,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@ExperimentalCoilApi
@Composable
fun MovieListItem(item: MovieEntity) {
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        CustomDialog(openDialogCustom = showDialog)
    }

    val cardBgColor = colorResource(id = R.color.material_blue_grey_50)
    val imageSize = "w300"
    val imageUrl = BuildConfig.BASE_URL_IMG + imageSize + item.posterPath
    Log.d("imageUrl-", imageUrl)
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        onClick = {
            showDialog.value = true
        },
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
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
                    text = item.title
                )
                MyRatingBar(
                    modifier = Modifier.width(16.dp),
                    rating = item.voteAverage.toDouble()
                )
                Text(
                    text = item.releaseDate,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun DropDownList(viewModel: MovieListViewModel) {
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
                        viewModel.getMovieList(false, ListType.valueOf(label))
                        expanded = false
                    }
                ) {
                    Text(text = label)
                }
            }
        }
    }
}