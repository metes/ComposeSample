package com.compose.ui.screen

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.compose.ui.MainViewModel


@Composable
fun ProfileScreen(navController: NavController, viewModel: MainViewModel) {
    /*...*/
    Button(onClick = {
        // navController.navigate("friends")
    }) {
        Text(text = "Navigate next friend")
    }
    /*...*/
}