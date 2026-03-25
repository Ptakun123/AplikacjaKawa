package com.example.myapplication.kawaview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.AppDatabase
import com.example.myapplication.Kawa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddKawaView(
    db: AppDatabase,
    onNavigateToWypicie: () -> Unit) {
    // Odpowiednik zmiennej w klasie, ale "pamiętający" stan podczas odświeżania UI
    var kraj by remember { mutableStateOf("") }
    var nazwa by remember { mutableStateOf("") }
    var palenie by remember { mutableStateOf("") }
    var gatunek by remember { mutableStateOf("") }
    var opis by remember { mutableStateOf("") }
    var lista by remember { mutableStateOf("") }
    val snackBarHostState = remember { SnackbarHostState() }
    // Główny kontener (odpowiednik pionowego LinearLayout)
    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackBarHostState)}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)// match_parent w obu kierunkach
                .padding(24.dp),         // padding od krawędzi ekranu
            //verticalArrangement = Arrangement.Center, // Wyśrodkuj w pionie
            horizontalAlignment = Alignment.CenterHorizontally // Wyśrodkuj w poziomie
        ) {
            NazwaTextField(value = nazwa, onValueChange = { text -> nazwa = text })
            KrajTextField(value = kraj, onValueChange = { text -> kraj = text })
            PalenieTextField(value = palenie, onValueChange = { text -> palenie = text })
            GatunekTextField(value = gatunek, onValueChange = { text -> gatunek = text })
            OpisTextField(value = opis, onValueChange = { text -> opis = text })
            DodajKaweButton(kraj, nazwa, palenie, gatunek, opis, db, snackBarHostState = snackBarHostState)
            ListaKawText(db)
            GoToWypicieButton({ onNavigateToWypicie() })
        }
    }
}
@Composable
fun DodajKaweButton(
    kraj: String,
    nazwa: String,
    palenie: String,
    gatunek: String,
    opis: String,
    db: AppDatabase,
    snackBarHostState: SnackbarHostState
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            keyboardController?.hide()
            scope.launch(Dispatchers.IO) {
                val kawaDao = db.kawaDao()
                if(kawaDao.countNazwa(nazwa) > 0){
                    snackBarHostState.showSnackbar(
                        message = "Kawa o nazwie '$nazwa' już istnieje",
                        duration = SnackbarDuration.Short
                    )
                }
                else {
                    val kawaObj = Kawa(0, kraj, nazwa, palenie, gatunek, opis)
                    kawaDao.insertAll(listOf(kawaObj))
                    snackBarHostState.showSnackbar(
                        message = "Dodano kawę $nazwa",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    ){
        Text("Dodaj Kawę")
    }
}
@Composable
fun ListaKawText(db: AppDatabase){
    val kawaList by db.kawaDao().getAll().collectAsStateWithLifecycle(initialValue = emptyList())
    val text: String
    if(kawaList.isEmpty()) text = "Brak Kaw w bazie"
    else text =
        kawaList.joinToString() {
                kawa -> "Nazwa: ${kawa.Nazwa}, Kraj:${kawa.Kraj}, Palenie: ${kawa.Palenie}, Gatunek: ${kawa.Gatunek}, Opis: ${kawa.Opis} \n"
        }
    Text(text)
}
@Composable
fun GoToWypicieButton(onNavigateToWypicie: () -> Unit){
    Button(onClick =
        {onNavigateToWypicie()}){
        Text("Dodaj Wypicie")
    }
}
