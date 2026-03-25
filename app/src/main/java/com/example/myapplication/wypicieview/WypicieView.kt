package com.example.myapplication.wypicieview

import android.net.ParseException
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.AppDatabase
import com.example.myapplication.ColorChangingButton
import com.example.myapplication.Kawa
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun AddWypicieView(
    db: AppDatabase,
    onNavigateToKawa:() -> Unit
){
    val kawaList by db.kawaDao().getAll().collectAsStateWithLifecycle(initialValue = emptyList())
    var nazwa by remember { mutableStateOf("") }
    var gramy by remember { mutableStateOf("") }
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply{
        isLenient = false
    } }
    var dataValue by remember { mutableStateOf(formatter.format(Date())) }
    var mlynek by remember { mutableStateOf("")}
    var temp by remember { mutableStateOf("") }
    var cisn by remember { mutableStateOf("") }
    var czas by remember {mutableStateOf("")}
    var ocena by remember {mutableStateOf(0)}
    val nazwaReady by remember {
        derivedStateOf {
            kawaList.any { it.Nazwa.equals(nazwa, ignoreCase = false) }
        }
    }
    val regexGramy = remember { Regex("^\\d+([.,]\\d+)?\$") }
    val gramyReady = regexGramy.matches(gramy)
    val dateReady = remember(dataValue) {
        // 1. Szybkie odrzucenie błędnego formatu bez obciążania parsera wyjątkami
        val regex = Regex("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}\$")
        if (!regex.matches(dataValue)) {
            return@remember false
        }
        try {
            formatter.parse(dataValue) != null
        } catch (e: ParseException) {
            false
        }
    }



    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)// match_parent w obu kierunkach
                .padding(24.dp),         // padding od krawędzi ekranu
            //verticalArrangement = Arrangement.Center, // Wyśrodkuj w pionie
            horizontalAlignment = Alignment.CenterHorizontally // Wyśrodkuj w poziomie
        ) {
            NazwaKawyTextField(value = nazwa, onValueChange = { nazwa=it }, kawaList = kawaList)
            DataTextField(value = dataValue, onValueChange = { dataValue = it })
            GramyTextField(value = gramy, onValueChange =  {gramy = it})
            MlynekTextField(value = mlynek, onValueChange = {mlynek = it })
            TempTextField(value = temp, onValueChange = {temp = it})
            CisnTextField(value = cisn, onValueChange = {cisn = it})
            CzasTextField(value = czas, onValueChange = {czas = it})
            RatingStars(ratingOcena = ocena, onRatingChange = {ocena = it})
            ColorChangingButton()
            GoToKawaButton({onNavigateToKawa()})
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