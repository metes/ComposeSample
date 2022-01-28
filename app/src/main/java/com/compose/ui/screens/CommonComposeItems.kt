package com.compose.ui.screens

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
import com.compose.R


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
            Icon(
                painter = painterResource(id = R.drawable.ic_round_star_24),
                contentDescription = "star",
                modifier = modifier,
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}