package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.repository.AuthRepository
import kotlinx.serialization.Serializable
import com.example.myapplication.ui.kawaview.AddKawaView
import com.example.myapplication.ui.kawaview.KawaViewModel
import com.example.myapplication.ui.kawaview.KawaViewModelFactory
import com.example.myapplication.data.roomdatabase.AppDatabase
import com.example.myapplication.ui.loginview.AddLoginView
import com.example.myapplication.ui.loginview.LoginViewModel
import com.example.myapplication.ui.loginview.LoginViewModelFactory
import com.example.myapplication.ui.wypicieview.AddWypicieView
import com.example.myapplication.ui.wypicieview.WypicieViewModel
import com.example.myapplication.ui.wypicieview.WypicieViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = (application as MyApplication).appContainer
        setContent {
            MyApp(appContainer)
        }
    }
}
@Serializable
object AddKawa
@Serializable
object AddWypicie
@Serializable
object Login
@Composable
fun MyApp(appContainer: AppContainer) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Login){
        composable<AddWypicie> {
            val viewModel: WypicieViewModel = viewModel(factory = appContainer.wypicieViewModelFactory)
            AddWypicieView(viewModel, onNavigateToKawa = {
                navController.navigate(route = AddKawa)
            })
        }
        composable<AddKawa> {
            val viewModel: KawaViewModel = viewModel(factory = appContainer.kawaViewModelFactory)
            AddKawaView(viewModel, onNavigateToWypicie = {
                navController.navigate(route = AddWypicie)
            })
        }
        composable<Login> {
            val viewModel: LoginViewModel = viewModel(factory = appContainer.loginViewModelFactory)
            AddLoginView(viewModel, onNavigateToWypicie =  {
                navController.navigate(route = AddWypicie) {
                    popUpTo(Login) {
                        inclusive = true
                    }
                }
            })
        }
    }
}