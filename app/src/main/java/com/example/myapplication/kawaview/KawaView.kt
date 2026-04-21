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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.AppDatabase
import com.example.myapplication.Kawa
import com.example.myapplication.wypicieview.UiEventWypicie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddKawaView(
    viewModel: KawaViewModel,
    onNavigateToWypicie: () -> Unit) {

    val kawaList by viewModel.kawaList.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) { // 'Unit' sprawia, że nasłuch odpala się tylko raz przy starcie widoku
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEventKawa.ShowSnackbar -> {
                    // Compose pokazuje Snackbar w bezpieczny sposób
                    snackBarHostState.showSnackbar(message = event.message)
                }
                is UiEventKawa.NavigateBack -> {
                    // Tu w przyszłości możesz wywołać nawigację po sukcesie
                }
            }
        }
    }

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
            NazwaTextField(value = viewModel.nazwa, onValueChange = { text -> viewModel.nazwa = text })
            KrajTextField(value = viewModel.kraj, onValueChange = { text -> viewModel.kraj = text })
            PalenieTextField(value = viewModel.palenie, onValueChange = { text -> viewModel.palenie = text })
            GatunekTextField(value = viewModel.gatunek, onValueChange = { text -> viewModel.gatunek = text })
            OpisTextField(value = viewModel.opis, onValueChange = { text -> viewModel.opis = text })
            DodajKaweButton(onClick = {viewModel.zapiszKawe()})
            ListaKawText(kawaList)
            GoToWypicieButton({ onNavigateToWypicie() })
        }
    }
}

