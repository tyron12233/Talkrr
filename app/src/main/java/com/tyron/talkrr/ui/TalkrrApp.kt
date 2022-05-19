package com.tyron.talkrr.ui

import androidx.compose.animation.Crossfade
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.tyron.talkrr.ui.login.LoginScreen
import com.tyron.talkrr.ui.signin.SignInState
import org.koin.androidx.compose.getViewModel

@Composable
fun TalkrrApp(
    talkrrAppViewModel: TalkrrAppViewModel = getViewModel()
) {
    val userState by talkrrAppViewModel.userState.collectAsState()

    Crossfade(targetState = userState) { state ->
        when (state) {
            SignInState.LOADING -> CircularProgressIndicator()
            SignInState.LOGGED_NOT_VALID -> Test(talkrrAppViewModel)
            SignInState.LOGGED_VALID -> TODO()
            SignInState.NOT_CONNECTED -> LoginScreen()
        }
    }
}

@Composable
fun Test(talkrrAppViewModel: TalkrrAppViewModel) {

    Button(onClick = { FirebaseAuth.getInstance().signOut() }) {
        Text(text = talkrrAppViewModel.userValue?.toString() + " " + talkrrAppViewModel.userState.collectAsState().value.toString())

    }
}

@Composable
fun InstantGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            
        }
    }
}