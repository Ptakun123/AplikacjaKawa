package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import com.example.myapplication.kawaview.AddKawaView
import com.example.myapplication.wypicieview.AddWypicieView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = (application as MyApplication).db
        setContent {
            MyApp(db)
        }
    }
}
@Serializable
object AddKawa
@Serializable
object AddWypicie

@Composable
fun MyApp(db : AppDatabase) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = AddKawa){
        composable<AddWypicie> { backStackEntry ->
            AddWypicieView(db, onNavigateToKawa = {
                navController.navigate(route = AddKawa)
            })
        }
        composable<AddKawa> { backStackEntry ->
            AddKawaView(db, onNavigateToWypicie = {
                navController.navigate(route = AddWypicie)
            })
        }
    }
}


@Composable
fun ColorChangingButton(){
    var clr by remember {mutableStateOf(Color.Red)};
    Button(
        onClick = {
        if(clr == Color.Red) clr = Color.Yellow
            else clr =Color.Red
        },
        colors= ButtonDefaults.buttonColors(containerColor = clr)
        ){
        Text("Zmień mój kolor!", color = Color.Black)
    }
}
