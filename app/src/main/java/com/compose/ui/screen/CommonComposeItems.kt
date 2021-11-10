package com.compose.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.R


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
fun MyToolbar() {
    TopAppBar(
        title = {
            Text(text = LocalContext.current.getString(R.string.movies))
        },
        backgroundColor = Color.Blue
    )
}

@Composable
fun ShowAlertDialog(title: String = "", text: String? = "", dialogState: MutableState<Boolean>) {
    AlertDialog(
        modifier = Modifier.padding(24.dp),
        onDismissRequest = {
            dialogState.value = false
        },
        title = {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Text(text = text ?: "", fontSize = 16.sp)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogState.value = false
                }) {
                Text("Ok", style = TextStyle(color = Color.Black))
            }
        },
        backgroundColor = Color.White,
        contentColor = Color.Black
    )
}
