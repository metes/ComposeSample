@file:OptIn(ExperimentalComposeUiApi::class)

package com.compose.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter
import com.compose.R
import com.compose.db.entity.MovieEntity
import kotlin.math.ceil


@Composable
fun CustomDialog(
    customComposableView: @Composable() () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        customComposableView()
    }
}

//Layout
@Composable
fun MovieDetailDialogView(
    movieEntity: MovieEntity,
    closeDialog: () -> Unit
) {
    val modifier: Modifier = Modifier

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
    ) {
        Column(
            modifier = modifier.background(Color.White),
            verticalArrangement = Arrangement.Top
        ) {
            //.......................................................................
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    contentDescription = null, // decorative
                    contentScale = ContentScale.Fit,
                    painter = rememberImagePainter(
                        getImageFromMovieEntity(
                            movieEntity = movieEntity,
                            imageSize = 500
                        )
                    ),
                    modifier = Modifier
                        .aspectRatio(500f / 750f)
                        .fillMaxHeight()
                )

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = movieEntity.title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp),
                        color = colorResource(R.color.black),
                        style = MaterialTheme.typography.h6,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MyRatingBar(
                            modifier = Modifier.width(16.dp),
                            rating = ceil(movieEntity.voteAverage / 2.0)
                        )

                        Text(
                            text = movieEntity.voteAverage.toString(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 4.dp),
                            color = colorResource(R.color.material_grey_700),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }

                    Text(
                        text = movieEntity.originalLang,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp),
                        color = colorResource(R.color.material_grey_700),
                        style = MaterialTheme.typography.subtitle2
                    )

                    Text(
                        text = movieEntity.releaseDate,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp),
                        color = colorResource(R.color.material_grey_700),
                        style = MaterialTheme.typography.subtitle2
                    )

                    Text(
                        text = movieEntity.genreIds,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp),
                        color = colorResource(R.color.material_grey_700),
                        style = MaterialTheme.typography.subtitle2
                    )

                }
            }


            //.......................................................................
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(colorResource(R.color.material_grey_50)),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(
                    onClick = { closeDialog() }, //{ openDialogCustom.value = false }
                ) {
                    Text(
                        text = getString(resId = R.string.save_to_list),
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.material_grey_700),
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
                }

                TextButton(
                    onClick = { closeDialog() }, //{ openDialogCustom.value = false }
                ) {
                    Text(
                        text = getString(android.R.string.ok),
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(R.color.material_grey_700),
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
                }
            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Preview(name = "Custom Dialog")
@Composable
fun MyDialogUIPreview() {
    MovieDetailDialogView(
        movieEntity = getEmptyMovieEntity()
    ) {
        // todo
    }
}
