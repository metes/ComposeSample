package com.compose.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.compose.BuildConfig
import com.compose.R
import com.compose.constants.APIConstants.EMPTY
import com.compose.constants.APIConstants.ZERO_FLOAT
import com.compose.constants.APIConstants.ZERO_INT
import com.compose.db.entity.MovieEntity


@Composable
fun ShowIndicator() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun getImageFromMovieEntity(movieEntity: MovieEntity, imageSize: Int = 300): String {
    val imageUrl = BuildConfig.BASE_URL_IMG + "w$imageSize" + movieEntity.posterPath
    Log.d("imageUrl-", imageUrl)

    return imageUrl
}

@Composable
fun getEmptyMovieEntity(): MovieEntity {
    return MovieEntity(
        0,
        EMPTY,
        EMPTY,
        EMPTY,
        ZERO_FLOAT,
        EMPTY,
        EMPTY,
        EMPTY,
        EMPTY,
        ZERO_INT,
        EMPTY
    )
}

@Composable
fun getString(resId: Int): String {
    return LocalContext.current.getString(resId)
}

@Composable
fun MyToolbar() {
    TopAppBar(
        title = {
            Text(text = LocalContext.current.getString(R.string.movies),
            color = colorResource(id = R.color.material_white)
            )
        },
        backgroundColor = colorResource(id = R.color.material_blue_500)
    )
}

@ExperimentalComposeUiApi
@Composable
fun MyRatingBar(
    modifier: Modifier = Modifier,
    rating: Double
) {
    val ratingState by remember {
        mutableStateOf(rating)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            val starResId = when {
                ratingState > i ->
                    R.drawable.ic_baseline_star_24
                kotlin.math.ceil(ratingState) > i ->
                    R.drawable.ic_baseline_star_half_24
                else ->
                    R.drawable.ic_baseline_star_outline_24
            }
            Icon(
                painter = painterResource(id = starResId),
                contentDescription = "star",
                modifier = modifier,
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}