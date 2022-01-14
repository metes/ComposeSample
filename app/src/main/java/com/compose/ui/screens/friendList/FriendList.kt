package com.compose.ui.screens.friendList

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun ProfileScreen(navController: NavController) {
    /*...*/
    Button(onClick = {
        // navController.navigate("friends")
    }) {
        Text(text = "Navigate next friend")
    }
    /*...*/
}