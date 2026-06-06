package com.example.myapplication.ui.loginview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthResponse
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.AuthResult
import com.example.myapplication.data.roomdatabase.AppDatabase
import com.example.myapplication.ui.kawaview.KawaViewModel
import com.example.myapplication.ui.kawaview.UiEventKawa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(private val repository: AuthRepository) : ViewModel(){
    var user by mutableStateOf("")
    var pass by mutableStateOf("")
    // Prywatny stan mutowalny
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    // Publiczny stan tylko do odczytu dla UI
    val loginState = _loginState.asStateFlow()

    private val eventChannel = Channel<UiEventLogin>()
    val uiEvents = eventChannel.receiveAsFlow()
    fun onLoginClicked(user: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            when (val result = repository.login(user, pass)) {
                is AuthResult.Success -> {
                    _loginState.value=LoginUiState.Success
                }
                is AuthResult.Error -> {
                    _loginState.value = LoginUiState.Idle
                    eventChannel.send(UiEventLogin.ShowSnackbar(result.message))
                }
                is AuthResult.NetworkError ->{
                    _loginState.value=LoginUiState.Idle
                    eventChannel.send(UiEventLogin.ShowSnackbar("Network Error"))
                }

            }
        }
    }
    fun onRegisterClicked(user: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            // Mapowanie wyniku z repozytorium na stan UI
            when (val result = repository.register(user, pass)) {
                is AuthResult.Success -> {
                    _loginState.value=LoginUiState.Success
                }
                is AuthResult.Error -> {
                    _loginState.value = LoginUiState.Idle
                    eventChannel.send(UiEventLogin.ShowSnackbar(result.message))
                }
                is AuthResult.NetworkError ->{
                    _loginState.value=LoginUiState.Idle
                    eventChannel.send(UiEventLogin.ShowSnackbar("Network Error"))
                }

            }
        }
    }

}
sealed class LoginUiState {
    object Idle : LoginUiState()    // Nic się nie dzieje
    object Loading : LoginUiState() // Kręci się kółko
    object Success : LoginUiState()
}
sealed interface UiEventLogin { //Snackbar
    data class ShowSnackbar(val message: String) : UiEventLogin
    object NavigateBack : UiEventLogin // Na przyszłość, np. żeby wyjść z ekranu po zapisie
}

class LoginViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}