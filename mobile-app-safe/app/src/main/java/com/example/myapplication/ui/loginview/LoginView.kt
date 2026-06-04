package com.example.myapplication.ui.loginview

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.kawaview.UiEventKawa
import com.google.android.material.progressindicator.CircularProgressIndicator

@Composable
fun AddLoginView(
    viewModel: LoginViewModel,
    onNavigateToWypicie: () -> Unit) {

    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(loginState) {
        if (loginState is LoginUiState.Success) {
            onNavigateToWypicie()
        }
    }
    LaunchedEffect(Unit) { // 'Unit' sprawia, że nasłuch odpala się tylko raz przy starcie widoku
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEventLogin.ShowSnackbar -> {
                    // Compose pokazuje Snackbar w bezpieczny sposób
                    snackBarHostState.showSnackbar(message = event.message)
                }
                is UiEventLogin.NavigateBack -> {
                    // Tu w przyszłości możesz wywołać nawigację po sukcesie
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState)}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)// match_parent w obu kierunkach
                .padding(24.dp),         // padding od krawędzi ekranu
            horizontalAlignment = Alignment.CenterHorizontally // Wyśrodkuj w poziomie
        ){
            when(loginState) {
                is LoginUiState.Idle -> {
                    TextField(value = viewModel.user,
                        onValueChange = {text -> viewModel.user = text},
                        label = {Text("Login")},
                        placeholder = {Text("Wpisz swój login")}
                    )
                    TextField(value = viewModel.pass    ,
                        onValueChange = {text -> viewModel.pass = text},
                        label = {Text("Hasło")},
                        placeholder = {Text("Wpisz swoje hasło")},
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Button(
                        onClick = {viewModel.onLoginClicked(viewModel.user, viewModel.pass)}
                    ){
                        Text("Login")
                    }
                    Button(
                        onClick = {viewModel.onRegisterClicked(viewModel.user, viewModel.pass)}
                    ){
                        Text("Zarejestruj")
                    }
                }
                is LoginUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is LoginUiState.Success -> {}
            }

        }
    }
}