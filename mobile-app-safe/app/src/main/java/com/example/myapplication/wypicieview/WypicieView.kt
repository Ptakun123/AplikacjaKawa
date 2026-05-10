package com.example.myapplication.wypicieview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
@Composable
fun AddWypicieView(
    viewModel: WypicieViewModel,
    onNavigateToKawa:() -> Unit
){
    val kawaList by viewModel.kawaList.collectAsStateWithLifecycle()
    val wypicieList by viewModel.wypicieList.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) { // 'Unit' sprawia, że nasłuch odpala się tylko raz przy starcie widoku
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEventWypicie.ShowSnackbar -> {
                    // Compose pokazuje Snackbar w bezpieczny sposób
                    snackBarHostState.showSnackbar(message = event.message)
                }
                is UiEventWypicie.NavigateBack -> {
                    // Tu w przyszłości możesz wywołać nawigację po sukcesie
                }
            }
        }
    }



    Scaffold(snackbarHost = {SnackbarHost(hostState = snackBarHostState)}) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)// match_parent w obu kierunkach
                .padding(24.dp),         // padding od krawędzi ekranu
            //verticalArrangement = Arrangement.Center, // Wyśrodkuj w pionie
            horizontalAlignment = Alignment.CenterHorizontally // Wyśrodkuj w poziomie
        ) {
            NazwaKawyTextField(value = viewModel.nazwa, onValueChange = { viewModel.nazwa=it }, kawaList = kawaList, viewModel.nazwaReady)
            DataTextField(value = viewModel.dataValue, onValueChange = { viewModel.dataValue = it }, viewModel.dataReady)
            GramyTextField(value = viewModel.gramy, onValueChange =  {viewModel.gramy = it}, viewModel.gramyReady)
            MlynekTextField(value = viewModel.mlynek, onValueChange = {viewModel.mlynek = it }, viewModel.mlynekReady)
            TempTextField(value = viewModel.temp, onValueChange = {viewModel.temp = it}, viewModel.tempReady)
            CisnTextField(value = viewModel.cisn, onValueChange = {viewModel.cisn = it}, viewModel.cisnReady)
            CzasTextField(value = viewModel.czas, onValueChange = {viewModel.czas = it}, viewModel.czasReady)
            RatingStars(ratingOcena = viewModel.ocena, onRatingChange = {viewModel.ocena = it})
            DodajWypicieButton(viewModel.ready, onClick = {viewModel.zapiszWypicie()})
            GoToKawaButton({onNavigateToKawa()})
            ListaWypicieText(wypicieList)
        }
    }
}
@Composable
fun GoToKawaButton(onNavigateToKawa: () -> Unit){
    Button(onClick =
        {onNavigateToKawa()}){
        Text("Dodaj Kawe")
    }
}