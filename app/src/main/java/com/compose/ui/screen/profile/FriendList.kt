package com.compose.ui.screen.profile

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
        Text(text = "Navigate test")
    }
    /*...*/
}